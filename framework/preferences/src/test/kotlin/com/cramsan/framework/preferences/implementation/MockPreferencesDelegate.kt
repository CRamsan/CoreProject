package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesDelegate

/**
 * @Author cramsan
 * @created 1/16/2021
 */
class MockPreferencesDelegate : PreferencesDelegate {

    val map = mutableMapOf<String, Any?>()

    override fun saveString(key: String, value: String?) {
        map[key] = value
    }

    override fun loadString(key: String): String? {
        return map[key] as String?
    }

    override fun saveInt(key: String, value: Int?) {
        map[key] = value
    }

    override fun loadInt(key: String): Int? {
        return map[key] as Int?
    }

    override fun saveLong(key: String, value: Long?) {
        map[key] = value
    }

    override fun loadLong(key: String): Long? {
        return map[key] as Long?
    }
}
