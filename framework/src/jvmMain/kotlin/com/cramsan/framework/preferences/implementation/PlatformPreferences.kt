package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PlatformPreferencesInterface

actual class PlatformPreferences : PlatformPreferencesInterface {

    override fun saveString(key: String, value: String) {
        TODO("not implemented")
    }

    override fun loadString(key: String): String? {
        TODO("not implemented")
    }

    override fun saveInt(key: String, value: Int) {
        TODO("not implemented")
    }

    override fun loadInt(key: String): Int? {
        TODO("not implemented")
    }

    override fun saveLong(key: String, value: Long) {
        TODO("not implemented")
    }

    override fun loadLong(key: String): Long? {
        TODO("not implemented")
    }
}
