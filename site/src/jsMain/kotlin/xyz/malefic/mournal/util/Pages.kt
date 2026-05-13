package xyz.malefic.mournal.util

enum class Pages(
    val value: String,
    val route: String,
) {
    INDEX("Today", "/"),
    SEARCH("Search", "/search"),
    MANAGE("Manage", "/manage"),
}
