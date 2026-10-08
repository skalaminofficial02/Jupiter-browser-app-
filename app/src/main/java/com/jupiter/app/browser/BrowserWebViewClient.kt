package com.jupiter.app.browser

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class BrowserWebViewClient(
    private val onUrlChanged: (String) -> Unit,
    private val onPageDone: (String, String) -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        url?.let(onUrlChanged)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (url != null) {
            onUrlChanged(url)
            onPageDone(url, view?.title ?: url)
        }
    }
}
