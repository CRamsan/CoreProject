package com.cramsan.ps2link.remoteconfig

/**
 * Instance of [RemoteConfigData] for use as a fallback if the remote config payload cannot be retrieved.
 */
val defaultConfigPayload = RemoteConfigData(
    listOf(
        "planetside2",
        "WrelPlays",
    ),
)
