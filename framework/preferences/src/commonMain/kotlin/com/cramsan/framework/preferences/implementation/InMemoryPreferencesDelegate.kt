package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesDelegate

/**
 * In-memory implementation of [PreferencesDelegate]. The data is expected to be cleared when the process ends.
 * This is designed to be used for fast access of run-time preferences.
 *
 * @Author cramsan
 * @created 11/22/2022
 */
class InMemoryPreferencesDelegate : PreferencesDelegate {

    private val map = mutableMapOf<String, Any?>()

    override fun saveString(key: String, value: String?) {
        map[key] = value
    }

    override fun loadString(key: String): String? {
        return map[key] as String?
    }

    override fun saveInt(key: String, value: Int) {
        map[key] = value
    }

    override fun loadInt(key: String): Int? {
        return map[key] as? Int
    }

    override fun saveLong(key: String, value: Long) {
        map[key] = value
    }

    override fun loadLong(key: String): Long? {
        return map[key] as? Long
    }

    override fun saveBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun loadBoolean(key: String): Boolean? {
        return map[key] as? Boolean
    }

    override fun remove(key: String) {
        map.remove(key)
    }
}
