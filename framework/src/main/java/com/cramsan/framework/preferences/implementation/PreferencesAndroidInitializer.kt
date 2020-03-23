package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.PreferencesPlatformInitializerInterface

class PreferencesAndroidInitializer(
    override val platformPreferences: PreferencesInterface
) : PreferencesPlatformInitializerInterface
