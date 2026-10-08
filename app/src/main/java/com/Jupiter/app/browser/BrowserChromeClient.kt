package com.jupiter.app.browser

import android.webkit.WebChromeClient
import android.webkit.WebView

class BrowserChromeClient(
    private val onProgress: (Int) -> Unit
) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgress(newProgress)
    }
}
