package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.PreferencesInterface

class Preferences(override val platformDelegate: PreferencesDelegate) : PreferencesInterface {

    override fun saveString(key: String, value: String) {
        platformDelegate.saveString(key, value)
    }

    override fun loadString(key: String): String? {
        return platformDelegate.loadString(key)
    }

    override fun saveInt(key: String, value: Int) {
        platformDelegate.saveInt(key, value)
    }

    override fun loadInt(key: String): Int? {
        return platformDelegate.loadInt(key)
    }

    override fun saveLong(key: String, value: Long) {
        platformDelegate.saveLong(key, value)
    }

    override fun loadLong(key: String): Long? {
        return platformDelegate.loadLong(key)
    }
}