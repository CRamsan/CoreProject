package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer
import com.cramsan.framework.preferences.PreferencesPlatformInitializerInterface

class PreferencesInitializer(val platformPreferences: PreferencesPlatformInitializerInterface) :
    BaseModuleInitializer<PreferencesManifest>(platformPreferences)
