package xyz.malefic.mournal.api

import kotlinx.browser.window
import org.w3c.dom.get
import kotlin.js.Date

private const val API_KEY_STORAGE_KEY = "mournal.api.key"

fun readSavedApiKey(): String = window.localStorage[API_KEY_STORAGE_KEY] ?: ""

fun saveApiKey(apiKey: String) {
    if (apiKey.isBlank()) {
        window.localStorage.removeItem(API_KEY_STORAGE_KEY)
    } else {
        window.localStorage.setItem(API_KEY_STORAGE_KEY, apiKey.trim())
    }
}

fun todayIsoDate(): String = Date().toISOString().slice(0..9)
