package xyz.malefic.mournal.api

import kotlin.js.JSON
import kotlin.js.json

object MainApi {
    suspend fun getEntriesForDate(date: String): List<Entry> = getEntries("${DAILY_MALEFIC_BASE_URL}/entry?date=$date")

    suspend fun getHistory(): List<Entry> = getEntries("${DAILY_MALEFIC_BASE_URL}/entry/history")

    suspend fun upsertEntry(
        payload: EntryPayload,
        apiKey: String,
    ): Entry? {
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
        val response =
            request(
                url = "${DAILY_MALEFIC_BASE_URL}/entry",
                method = "POST",
                body = body,
                apiKey = apiKey,
            )
        return when (response.status) {
            200 -> parseEntry(response.body)
            else -> throwApiError(response.status, response.body)
        }
    }

    suspend fun deleteEntry(
        id: Long,
        apiKey: String,
    ) {
        val body = JSON.stringify(json("id" to id))
        val response =
            request(
                url = "${DAILY_MALEFIC_BASE_URL}/entry",
                method = "DELETE",
                body = body,
                apiKey = apiKey,
            )
        when (response.status) {
            200, 204 -> Unit
            else -> throwApiError(response.status, response.body)
        }
    }
}

private suspend fun getEntries(url: String): List<Entry> {
    val response = request(url)
    return when (response.status) {
        200 -> parseEntries(response.body)
        204 -> emptyList()
        else -> throwApiError(response.status, response.body)
    }
}

private fun throwApiError(
    status: Int,
    body: String,
): Nothing {
    val fallback = "Request failed ($status)"
    throw IllegalStateException(body.ifBlank { fallback })
}
