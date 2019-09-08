package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PlatformPreferencesInterface

actual class PlatformPreferences : PlatformPreferencesInterface {
    override fun saveString(key: String, value: String, parameters: PreferencesParameters) {
        TODO("not implemented")
    }

    override fun loadString(key: String, parameters: PreferencesParameters): String? {
        TODO("not implemented")
    }
}
