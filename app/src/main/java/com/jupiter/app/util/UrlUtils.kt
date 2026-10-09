package com.jupiter.app.util

import android.net.Uri

object UrlUtils {
    const val HOME = "https://jupiter-search-7cs.pages.dev"
    private const val SEARCH = "https://jupiter-search-7cs.pages.dev/search?q="

    fun resolve(input: String): String {
        val text = input.trim()
        return when {
            text.isEmpty() -> HOME
            text.startsWith("http://") || text.startsWith("https://") -> text
            text.contains(" ") || !text.contains(".") -> SEARCH + Uri.encode(text)
            else -> "https://$text"
        }
    }
}
