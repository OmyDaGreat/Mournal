package xyz.malefic.mournal.util

import xyz.malefic.mournal.components.Icon

enum class Pages(
    val value: String,
    val route: String,
    val icon: Icon,
) {
    INDEX("Home", "/", Icon.HOME),
    SEARCH("Search", "/search", Icon.SEARCH),
    MANAGE("Manage", "/manage", Icon.BUILD),
}
