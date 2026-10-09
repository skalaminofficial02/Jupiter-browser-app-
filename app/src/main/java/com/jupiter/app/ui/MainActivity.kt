package com.jupiter.app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.Toast
import com.jupiter.app.browser.BrowserChromeClient
import com.jupiter.app.browser.BrowserWebViewClient
import com.jupiter.app.browser.DownloadHandler
import com.jupiter.app.browser.TabManager
import com.jupiter.app.data.Entry
import com.jupiter.app.data.EntryStore
import com.jupiter.app.databinding.ActivityMainBinding
import com.jupiter.app.util.UrlUtils

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookmarks: EntryStore
    private lateinit var history: EntryStore
    private lateinit var tabs: TabManager

    private val web: WebView? get() = tabs.current?.webView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarks = EntryStore(this, "bookmarks", 500)
        history = EntryStore(this, "history", 100)

        tabs = TabManager(
            container = binding.webContainer,
            factory = { createWebView() },
            onChanged = { tab ->
                binding.urlBar.setText(tab.webView.url ?: "")
                binding.btnTabs.text = tabs.count.toString()
            }
        )

        setupUrlBar()
        setupButtons()

        tabs.open(intent?.dataString ?: UrlUtils.HOME)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView(): WebView {
        val wv = WebView(this)
        wv.settings.javaScriptEnabled = true
        wv.settings.domStorageEnabled = true
        wv.settings.builtInZoomControls = true
        wv.settings.displayZoomControls = false
        wv.setDownloadListener(DownloadHandler(this))
        wv.webViewClient = BrowserWebViewClient(
            onUrlChanged = { url ->
                if (web === wv) binding.urlBar.setText(url)
            },
            onPageDone = { url, title -> history.add(Entry(title, url)) }
        )
        wv.webChromeClient = BrowserChromeClient { progress ->
            if (web === wv) {
                binding.progressBar.progress = progress
                binding.progressBar.visibility =
                    if (progress < 100) View.VISIBLE else View.GONE
            }
        }
        return wv
    }

    private fun setupUrlBar() {
        binding.urlBar.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                web?.loadUrl(UrlUtils.resolve(view.text.toString()))
                view.clearFocus()
                true
            } else false
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { web?.let { if (it.canGoBack()) it.goBack() } }
        binding.btnForward.setOnClickListener { web?.let { if (it.canGoForward()) it.goForward() } }
        binding.btnReload.setOnClickListener { web?.reload() }
        binding.btnHome.setOnClickListener { web?.loadUrl(UrlUtils.HOME) }
        binding.btnBookmark.setOnClickListener { toggleBookmark() }
        binding.btnTabs.setOnClickListener { showTabs() }
        binding.btnMenu.setOnClickListener { showMenu() }
    }

    private fun toggleBookmark() {
        val url = web?.url ?: return
        if (bookmarks.contains(url)) {
            bookmarks.remove(url)
            toast("Bookmark removed")
        } else {
            bookmarks.add(Entry(web?.title ?: url, url))
            toast("Bookmark added")
        }
    }

    private fun showTabs() {
        AlertDialog.Builder(this)
            .setTitle("Tabs (${tabs.count})")
            .setItems(tabs.titles.toTypedArray()) { _, i -> tabs.select(i) }
            .setPositiveButton("+ New tab") { _, _ -> tabs.open(UrlUtils.HOME) }
            .setNeutralButton("Close current") { _, _ ->
                tabs.closeCurrent()
                binding.btnTabs.text = tabs.count.toString()
            }
            .setNegativeButton("Back", null)
            .show()
    }

    private fun showMenu() {
        val items = arrayOf("New tab", "Bookmarks", "History", "Clear history")
        AlertDialog.Builder(this)
            .setTitle("Jupiter")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> tabs.open(UrlUtils.HOME)
                    1 -> showList("Bookmarks", bookmarks)
                    2 -> showList("History", history)
                    3 -> {
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
                web?.loadUrl(list[i].url)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val w = web
        when {
            w != null && w.canGoBack() -> w.goBack()
            tabs.count > 1 -> {
                tabs.closeCurrent()
                binding.btnTabs.text = tabs.count.toString()
            }
            else -> super.onBackPressed()
        }
    }
}
