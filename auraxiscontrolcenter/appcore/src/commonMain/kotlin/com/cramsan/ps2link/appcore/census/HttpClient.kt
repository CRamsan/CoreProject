package com.cramsan.ps2link.appcore.census

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer

/**
 * @Author cramsan
 * @created 1/18/2021
 */

fun buildHttpClient(): HttpClient {
    return HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }
}
