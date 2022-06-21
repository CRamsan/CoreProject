package com.cramsan.ps2link.appcore.census

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * @Author cramsan
 * @created 1/18/2021
 */

fun buildHttpClient(jsonSerializer: Json): HttpClient {
    return HttpClient {
        install(HttpCache)
        install(ContentNegotiation) {
            json(jsonSerializer)
        }
    }
}
