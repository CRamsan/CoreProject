package com.cramsan.ps2link.remoteconfig

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RemoteConfigData(
    val twitterUsernames: List<String>,
    val featureFlag: Map<String, FeatureFlag<Boolean>>,
)

val remoteConfigJson = Json {
    ignoreUnknownKeys = true
}
