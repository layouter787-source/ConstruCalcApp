package com.lay.construcalc.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class HistoryEntry(
    val id: Long,
    val calculator: String,
    val inputs: String,
    val result: String,
    val timestamp: Long
)

class HistoryRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("construcalc_history", Context.MODE_PRIVATE)
    private val key = "entries"

    fun getAll(): List<HistoryEntry> = runCatching {
        val array = JSONArray(prefs.getString(key, "[]") ?: "[]")
        buildList {
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                add(HistoryEntry(o.optLong("id"), o.optString("calculator"), o.optString("inputs"), o.optString("result"), o.optLong("timestamp")))
            }
        }
    }.getOrDefault(emptyList())

    fun add(entry: HistoryEntry) {
        val entries = (listOf(entry) + getAll()).take(30)
        val array = JSONArray()
        entries.forEach {
            array.put(JSONObject().apply {
                put("id", it.id)
                put("calculator", it.calculator)
                put("inputs", it.inputs)
                put("result", it.result)
                put("timestamp", it.timestamp)
            })
        }
        prefs.edit().putString(key, array.toString()).apply()
    }

    fun clear() = prefs.edit().remove(key).apply()
}
