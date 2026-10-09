package com.jupiter.app.browser

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.ByteArrayInputStream

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

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if (AdBlocker.isBlocked(request?.url?.host)) {
            return WebResourceResponse(
                "text/plain", "utf-8", ByteArrayInputStream(ByteArray(0))
            )
        }
        return super.shouldInterceptRequest(view, request)
    }
}
