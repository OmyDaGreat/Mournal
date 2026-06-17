package xyz.malefic.mournal.api

import kotlin.js.json

object MainApi {
    private const val DAILY_MALEFIC_API_URL = "https://daily.malefic.xyz/api"

    suspend fun getLatestEntries(): List<Entry> = getEntries("${DAILY_MALEFIC_API_URL}/entries?latest=true")

    suspend fun getAllEntries(): List<Entry> = getEntries("${DAILY_MALEFIC_API_URL}/entries")

    suspend fun upsertEntry(
        payload: EntryPayload,
        apiKey: String,
    ): Entry {
        val body =
            JSON.stringify(
                json(
                    "author" to payload.author,
                    "text" to payload.text,
                    "date" to payload.date,
                    "songQuery" to payload.songQuery,
                ),
            )
        val response =
            if (payload.id == null) {
                request(
                    url = "${DAILY_MALEFIC_API_URL}/entries",
                    method = "POST",
                    body = body,
                    apiKey = apiKey,
                )
            } else {
                request(
                    url = "${DAILY_MALEFIC_API_URL}/entries/${payload.id}",
                    method = "PUT",
                    body = body,
                    apiKey = apiKey,
                )
            }
        return when (response.status) {
            200, 201 -> parseEntry(response.body)
            else -> throwApiError(response.status, response.body)
        }
    }

    suspend fun deleteEntry(
        id: Long,
        apiKey: String,
    ) {
        val response =
            request(
                url = "${DAILY_MALEFIC_API_URL}/entries/$id",
                method = "DELETE",
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
