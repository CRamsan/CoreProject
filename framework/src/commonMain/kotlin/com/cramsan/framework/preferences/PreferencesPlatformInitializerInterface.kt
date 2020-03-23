package com.cramsan.framework.preferences

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.preferences.implementation.PreferencesManifest

interface PreferencesPlatformInitializerInterface : BaseModulePlatformInitializerInterface<PreferencesManifest> {
    val platformPreferences: PreferencesInterface
}
