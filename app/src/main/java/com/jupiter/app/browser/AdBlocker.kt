package com.jupiter.app.browser

object AdBlocker {
    private val hosts = setOf(
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "adnxs.com",
        "taboola.com",
        "outbrain.com",
        "popads.net",
        "propellerads.com",
        "adsterra.com"
    )

    fun isBlocked(host: String?): Boolean {
        if (host == null) return false
        return hosts.any { host == it || host.endsWith(".$it") }
    }
}
