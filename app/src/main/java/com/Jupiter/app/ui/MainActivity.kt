package com.jupiter.app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.jupiter.app.browser.BrowserChromeClient
import com.jupiter.app.browser.BrowserWebViewClient
import com.jupiter.app.data.Entry
import com.jupiter.app.data.EntryStore
import com.jupiter.app.databinding.ActivityMainBinding
import com.jupiter.app.util.UrlUtils

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookmarks: EntryStore
    private lateinit var history: EntryStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarks = EntryStore(this, "bookmarks", 500)
        history = EntryStore(this, "history", 100)

        setupWebView()
        setupUrlBar()
        setupButtons()

        binding.webView.loadUrl(intent?.dataString ?: UrlUtils.HOME)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            webViewClient = BrowserWebViewClient(
                onUrlChanged = { url -> binding.urlBar.setText(url) },
                onPageDone = { url, title -> history.add(Entry(title, url)) }
            )
            webChromeClient = BrowserChromeClient { progress ->
                binding.progressBar.progress = progress
                binding.progressBar.visibility =
                    if (progress < 100) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupUrlBar() {
        binding.urlBar.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                binding.webView.loadUrl(UrlUtils.resolve(view.text.toString()))
                view.clearFocus()
                true
            } else false
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            if (binding.webView.canGoBack()) binding.webView.goBack()
        }
        binding.btnForward.setOnClickListener {
            if (binding.webView.canGoForward()) binding.webView.goForward()
        }
        binding.btnReload.setOnClickListener { binding.webView.reload() }
        binding.btnHome.setOnClickListener { binding.webView.loadUrl(UrlUtils.HOME) }
        binding.btnBookmark.setOnClickListener { toggleBookmark() }
        binding.btnMenu.setOnClickListener { showMenu() }
    }

    private fun toggleBookmark() {
        val url = binding.webView.url ?: return
        if (bookmarks.contains(url)) {
            bookmarks.remove(url)
            toast("Bookmark removed")
        } else {
            bookmarks.add(Entry(binding.webView.title ?: url, url))
            toast("Bookmark added")
        }
    }

    private fun showMenu() {
        val items = arrayOf("Bookmarks", "History", "Clear history")
        AlertDialog.Builder(this)
            .setTitle("Jupiter")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> showList("Bookmarks", bookmarks)
                    1 -> showList("History", history)
                    2 -> {
                        history.clear()
                        toast("History cleared")
                    }
                }
            }
            .show()
    }

    private fun showList(title: String, store: EntryStore) {
        val list = store.all()
        if (list.isEmpty()) {
            toast("$title is empty")
            return
        }
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(list.map { it.title }.toTypedArray()) { _, i ->
                binding.webView.loadUrl(list[i].url)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack()
        else super.onBackPressed()
    }
}
