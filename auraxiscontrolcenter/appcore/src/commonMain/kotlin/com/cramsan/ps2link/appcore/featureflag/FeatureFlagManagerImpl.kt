package com.cramsan.ps2link.appcore.featureflag

import com.cramsan.framework.remoteconfig.RemoteConfig
import com.cramsan.ps2link.remoteconfig.RemoteConfigData
import kotlin.random.Random

/**
 * Implementation of the [FeatureFlagManager] that uses a [remoteConfig] as it's source.
 */
class FeatureFlagManagerImpl(
    private val remoteConfig: RemoteConfig<RemoteConfigData>,
) : FeatureFlagManager {

    override fun getFeatureValue(featureFlagKey: String, defaultValue: Boolean): Boolean {
        val featureFlag = remoteConfig
            .getConfigPayloadOrNull()
            ?.featureFlag
            ?.get(featureFlagKey) ?: return defaultValue

        val percentage = featureFlag.percentage
        val value = featureFlag.value
        val withinTreatment = Random.nextInt(1, 101) <= percentage
        return if (withinTreatment) {
            value
        } else {
            defaultValue
        }
    }
}
