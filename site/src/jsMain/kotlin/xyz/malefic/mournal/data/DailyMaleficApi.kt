package xyz.malefic.mournal.data

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.get
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import kotlin.js.JSON
import kotlin.js.json
import kotlin.js.unsafeCast

const val DailyMaleficBaseUrl = "http://100.125.223.81:7290"
private const val ApiKeyStorageKey = "mournal.api.key"

data class Artist(
    val id: String,
    val name: String,
)

data class Song(
    val id: String,
    val name: String,
    val artists: List<Artist>,
)

data class Entry(
    val id: String,
    val author: String,
    val text: String,
    val date: String,
    val song: Song?,
)

data class EntryPayload(
    val id: String? = null,
    val author: String,
    val text: String,
    val date: String,
    val songQuery: String? = null,
)

object DailyMaleficApi {
    suspend fun getEntriesForDate(date: String): List<Entry> = getEntries("${DailyMaleficBaseUrl}/entry?date=$date")

    suspend fun getHistory(): List<Entry> = getEntries("${DailyMaleficBaseUrl}/entry/history")

    suspend fun upsertEntry(
        payload: EntryPayload,
        apiKey: String,
    ): Entry {
        val body =
            JSON.stringify(
                json(
                    "id" to payload.id,
                    "author" to payload.author,
                    "text" to payload.text,
                    "date" to payload.date,
                    "songQuery" to payload.songQuery,
                ),
            )
        val rawResponse =
            request(
                url = "${DailyMaleficBaseUrl}/entry",
                method = "POST",
                body = body,
                apiKey = apiKey,
            )
        return parseEntries(rawResponse).first()
    }

    suspend fun deleteEntry(
        id: String,
        apiKey: String,
    ) {
        val body = JSON.stringify(json("id" to id))
        request(
            url = "${DailyMaleficBaseUrl}/entry",
            method = "DELETE",
            body = body,
            apiKey = apiKey,
        )
    }
}

fun readSavedApiKey(): String = window.localStorage[ApiKeyStorageKey] ?: ""

fun saveApiKey(apiKey: String) {
    if (apiKey.isBlank()) {
        window.localStorage.removeItem(ApiKeyStorageKey)
    } else {
        window.localStorage.setItem(ApiKeyStorageKey, apiKey.trim())
    }
}

fun todayIsoDate(): String = js("new Date().toISOString().slice(0, 10)") as String

private suspend fun getEntries(url: String): List<Entry> {
    val rawResponse = request(url)
    return parseEntries(rawResponse)
}

private suspend fun request(
    url: String,
    method: String = "GET",
    body: String? = null,
    apiKey: String? = null,
): String {
    val headers = Headers()
    headers.append("Accept", "application/json")
    if (body != null) {
        headers.append("Content-Type", "application/json")
    }
    if (!apiKey.isNullOrBlank()) {
        headers.append("X-API-Key", apiKey)
    }

    val init = js("{}").unsafeCast<RequestInit>()
    init.method = method
    init.headers = headers
    if (body != null) {
        init.body = body
    }

    val response = window.fetch(url, init).await()
    val text = response.text().await()
    if (!response.ok) {
        val fallback = "Request failed (${response.status.toInt()})"
        throw IllegalStateException(text.ifBlank { fallback })
    }
    return text
}

private fun parseEntries(rawResponse: String): List<Entry> {
    val payload = JSON.parse<dynamic>(rawResponse)
    return if (js("Array.isArray(payload)") as Boolean) {
        (payload as Array<dynamic>).map { parseEntry(it) }
    } else {
        listOf(parseEntry(payload))
    }
}

private fun parseEntry(raw: dynamic): Entry =
    Entry(
        id = raw.id?.toString() ?: "",
        author = raw.author?.toString() ?: "",
        text = raw.text?.toString() ?: "",
        date = raw.date?.toString() ?: "",
        song = parseSong(raw.song),
    )

private fun parseSong(raw: dynamic): Song? {
    if (raw == null) return null
    val artists =
        if (raw.artists != null && (js("Array.isArray(raw.artists)") as Boolean)) {
            (raw.artists as Array<dynamic>).map { artist ->
                Artist(
                    id = artist.id?.toString() ?: "",
                    name = artist.name?.toString() ?: "",
                )
            }
        } else {
            emptyList()
        }

    return Song(
        id = raw.id?.toString() ?: "",
        name = raw.name?.toString() ?: "",
        artists = artists,
    )
}
