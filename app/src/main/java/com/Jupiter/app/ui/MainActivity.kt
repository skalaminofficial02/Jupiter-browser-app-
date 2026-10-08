package com.Jupiter.app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.Jupiter.app.browser.BrowserChromeClient
import com.Jupiter.app.browser.BrowserWebViewClient
import com.Jupiter.app.databinding.ActivityMainBinding
import com.Jupiter.app.util.UrlUtils

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            webViewClient = BrowserWebViewClient { url -> binding.urlBar.setText(url) }
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
        binding.btnBack.setOnClickListener { if (binding.webView.canGoBack()) binding.webView.goBack() }
        binding.btnForward.setOnClickListener { if (binding.webView.canGoForward()) binding.webView.goForward() }
        binding.btnReload.setOnClickListener { binding.webView.reload() }
        binding.btnHome.setOnClickListener { binding.webView.loadUrl(UrlUtils.HOME) }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack()
        else super.onBackPressed()
    }
}
