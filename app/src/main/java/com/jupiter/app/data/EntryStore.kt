package com.jupiter.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Entry(val title: String, val url: String)

class EntryStore(
    context: Context,
    private val key: String,
    private val limit: Int
) {
    private val prefs =
        context.getSharedPreferences("jupiter_store", Context.MODE_PRIVATE)

    fun all(): List<Entry> {
        val arr = JSONArray(prefs.getString(key, "[]") ?: "[]")
        return (0 until arr.length()).map {
            val o = arr.getJSONObject(it)
            Entry(o.getString("title"), o.getString("url"))
        }
    }

    private fun save(list: List<Entry>) {
        val arr = JSONArray()
        list.forEach {
            arr.put(JSONObject().put("title", it.title).put("url", it.url))
        }
        prefs.edit().putString(key, arr.toString()).apply()
    }

    fun add(entry: Entry) {
        save((listOf(entry) + all().filter { it.url != entry.url }).take(limit))
    }

    fun remove(url: String) {
        save(all().filter { it.url != url })
    }

    fun contains(url: String): Boolean = all().any { it.url == url }

    fun clear() {
        prefs.edit().remove(key).apply()
    }
}
