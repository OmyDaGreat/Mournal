package xyz.malefic.mournal.api

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
    val id: Long,
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
