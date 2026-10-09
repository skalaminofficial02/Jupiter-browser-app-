package com.jupiter.app.browser

import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import com.jupiter.app.util.UrlUtils

class TabManager(
    private val container: FrameLayout,
    private val factory: () -> WebView,
    private val onChanged: (Tab) -> Unit
) {
    private val tabs = mutableListOf<Tab>()
    private var index = -1

    val current: Tab? get() = tabs.getOrNull(index)
    val count: Int get() = tabs.size
    val titles: List<String> get() = tabs.map { it.title }

    fun open(url: String) {
        tabs.add(Tab(factory()))
        select(tabs.lastIndex)
        current?.webView?.loadUrl(url)
    }

    fun select(i: Int) {
        if (i !in tabs.indices) return
        index = i
        container.removeAllViews()
        container.addView(
            tabs[i].webView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        onChanged(tabs[i])
    }

    fun closeCurrent() {
        if (index !in tabs.indices) return
        container.removeAllViews()
        tabs.removeAt(index).webView.destroy()
        if (tabs.isEmpty()) {
            index = -1
            open(UrlUtils.HOME)
        } else {
            select(minOf(index, tabs.lastIndex))
        }
    }
}
