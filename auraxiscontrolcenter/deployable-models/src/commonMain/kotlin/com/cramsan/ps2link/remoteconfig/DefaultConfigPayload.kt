package com.cramsan.ps2link.remoteconfig

/**
 * Instance of [RemoteConfigData] for use as a fallback if the remote config payload cannot be retrieved.
 */
val defaultConfigPayload = RemoteConfigData(
    twitterUsernames = listOf(
        "planetside2",
        "WrelPlays",
    ),
    featureFlag = mapOf(
        FeatureFlagKeys.RECORD_CW_METRICS to FeatureFlag(
            0.0f,
            true,
        ),
    ),
)
