package xyz.malefic.mournal.util

enum class Pages(
    val value: String,
    val route: String,
) {
    INDEX("Home", "/"),
    SEARCH("Search", "/search"),
    MANAGE("Manage", "/manage"),
}
