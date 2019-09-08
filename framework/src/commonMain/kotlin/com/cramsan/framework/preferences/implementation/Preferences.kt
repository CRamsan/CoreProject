package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesInterface

class Preferences(initializer: PreferencesInitializer) : PreferencesInterface {

    private val platformPreferences = initializer.platformPreferences

    override fun saveString(key: String, value: String) {
        platformPreferences.saveString(key, value)
    }

    override fun loadString(key: String): String? {
        return platformPreferences.loadString(key)
    }

    override fun saveInt(key: String, value: Int) {
        platformPreferences.saveInt(key, value)
    }

    override fun loadInt(key: String): Int? {
        return platformPreferences.loadInt(key)
    }

    override fun saveLong(key: String, value: Long) {
        platformPreferences.saveLong(key, value)
    }

    override fun loadLong(key: String): Long? {
        return platformPreferences.loadLong(key)
    }
}
