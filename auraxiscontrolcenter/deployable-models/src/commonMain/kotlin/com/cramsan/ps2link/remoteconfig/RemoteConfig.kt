package com.cramsan.ps2link.remoteconfig

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RemoteConfigData(
    val twitterUsernames: List<String>
)

val remoteConfigJson = Json {
    ignoreUnknownKeys = true
}

val remoteConfigPayload = RemoteConfigData(
    listOf(
        "planetside2",
        "PS2DailyDeals",
        "WrelPlays",
    )
)
