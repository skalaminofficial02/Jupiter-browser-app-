package com.jupiter.app.browser

import android.webkit.WebView

class Tab(val webView: WebView) {
    val title: String
        get() = webView.title?.takeIf { it.isNotBlank() } ?: webView.url ?: "New tab"
}
