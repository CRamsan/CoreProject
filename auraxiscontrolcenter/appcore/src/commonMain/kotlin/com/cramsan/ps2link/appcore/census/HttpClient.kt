package com.cramsan.ps2link.appcore.census

import io.ktor.client.HttpClient
import io.ktor.client.features.cache.HttpCache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

/**
 * @Author cramsan
 * @created 1/18/2021
 */

fun buildHttpClient(json: Json): HttpClient {
    return HttpClient {
        install(HttpCache)
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }
}
