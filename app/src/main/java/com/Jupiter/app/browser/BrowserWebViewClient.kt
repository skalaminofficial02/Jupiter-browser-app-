package com.Jupiter.app.browser

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class BrowserWebViewClient(
    private val onUrlChanged: (String) -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        url?.let(onUrlChanged)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        url?.let(onUrlChanged)
    }
}
