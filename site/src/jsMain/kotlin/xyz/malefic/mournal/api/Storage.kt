package xyz.malefic.mournal.api

import kotlinx.browser.window
import org.w3c.dom.get

private const val API_KEY_STORAGE_KEY = "mournal.api.key"

fun readSavedApiKey(): String = window.localStorage[API_KEY_STORAGE_KEY] ?: ""

fun saveApiKey(apiKey: String) {
    if (apiKey.isBlank()) {
        window.localStorage.removeItem(API_KEY_STORAGE_KEY)
    } else {
        window.localStorage.setItem(API_KEY_STORAGE_KEY, apiKey.trim())
    }
}

fun todayIsoDate(): String = js("new Date().toISOString().slice(0, 10)") as String
