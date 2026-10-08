package com.jupiter.app.util

import android.net.Uri

object UrlUtils {
    const val HOME = "https://duckduckgo.com"
    private const val SEARCH = "https://duckduckgo.com/?q="

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
