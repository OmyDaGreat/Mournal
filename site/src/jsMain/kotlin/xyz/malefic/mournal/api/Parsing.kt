package xyz.malefic.mournal.api

import kotlin.js.JSON

internal fun parseEntries(rawResponse: String): List<Entry> {
    val payload = JSON.parse<dynamic>(rawResponse)
    return if (js("Array.isArray(payload)") as Boolean) {
        (payload as Array<dynamic>).map { parseEntryPayload(it) }
    } else {
        listOf(parseEntryPayload(payload))
    }
}

internal fun parseEntry(rawResponse: String): Entry {
    val payload = JSON.parse<dynamic>(rawResponse)
    return parseEntryPayload(payload)
}

private fun parseEntryPayload(raw: dynamic): Entry =
    Entry(
        id = parseRequiredLong(raw.id, "id"),
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

private fun parseRequiredLong(
    value: dynamic,
    fieldName: String,
): Long {
    val asString = value?.toString() ?: throw IllegalStateException("Missing required field '$fieldName'.")
    return asString.toLongOrNull() ?: throw IllegalStateException("Invalid '$fieldName': $asString")
}
