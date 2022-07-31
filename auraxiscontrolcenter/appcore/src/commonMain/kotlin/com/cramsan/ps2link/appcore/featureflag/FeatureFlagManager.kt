package com.cramsan.ps2link.appcore.featureflag

/**
 * Class that abstracts retrieving the status of features that can be enabled or disabled.
 */
interface FeatureFlagManager {

    /**
     * Fetch the status of a flag identified with [featureFlagKey]. If the value cannot
     * be fetched, [defaultValue] will be used.
     */
    fun getFeatureValue(featureFlagKey: String, defaultValue: Boolean): Boolean
}
