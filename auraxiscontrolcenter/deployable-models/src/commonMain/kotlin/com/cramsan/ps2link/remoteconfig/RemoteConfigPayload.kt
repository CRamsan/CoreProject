package com.cramsan.ps2link.remoteconfig

/**
 * Instance of [RemoteConfigData] to be deployed to the remote config service.
 */
val remoteConfigPayload = RemoteConfigData(
    twitterUsernames = listOf(
        "planetside2",
        "WrelPlays",
        "PS2Central",
    ),
    featureFlag = mapOf(
        FeatureFlagKeys.RECORD_CW_METRICS to FeatureFlag(
            1f,
            true,
        ),
    ),
)
