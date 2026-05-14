@file:Suppress("ktlint:standard:filename")

package xyz.malefic.mournal.api

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import kotlin.js.unsafeCast

internal data class ApiResponse(
    val status: Int,
    val body: String,
)

internal suspend fun request(
    url: String,
    method: String = "GET",
    body: String? = null,
    apiKey: String? = null,
): ApiResponse {
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
    return ApiResponse(
        status = response.status.toInt(),
        body = text,
    )
}
