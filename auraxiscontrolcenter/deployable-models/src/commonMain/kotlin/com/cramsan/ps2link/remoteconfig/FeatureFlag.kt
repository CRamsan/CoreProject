package com.cramsan.ps2link.remoteconfig

import kotlinx.serialization.Serializable

/**
 * Data class representing the state of a feature. The [percentage] value will be used for client
 * side sampling, while the [value] is the value that the client will use if they are part of the
 * sample.
 */
@Serializable
data class FeatureFlag<T>(
    val percentage: Float,
    val value: T,
)

/**
 * List of strings to identify our features.
 */
object FeatureFlagKeys {
    const val RECORD_CW_METRICS = "RECORD_CW_METRICS"
}
