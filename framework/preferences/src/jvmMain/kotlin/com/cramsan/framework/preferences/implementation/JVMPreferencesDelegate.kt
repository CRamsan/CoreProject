package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesDelegate
import java.util.prefs.Preferences

/**
 * This implementation of [PreferencesDelegate] uses the JVM
 */
class JVMPreferencesDelegate : PreferencesDelegate {

    private val prefs: Preferences = Preferences.userNodeForPackage(JVMPreferencesDelegate::class.java)

    override fun saveString(key: String, value: String?) {
        if (value == null) {
            prefs.remove(key)
        } else {
            prefs.put(key, value)
        }
    }

    override fun loadString(key: String): String? {
        return prefs.get(key, null)
    }

    override fun saveInt(key: String, value: Int) {
        prefs.putInt(key, value)
    }

    @Suppress("SwallowedException")
    override fun loadInt(key: String): Int? {
        return try {
            prefs.getInt(key, KEY_NOT_FOUND)
        } catch (throwable: Throwable) {
            null
        }
    }

    override fun saveLong(key: String, value: Long) {
        prefs.putLong(key, value)
    }

    @Suppress("SwallowedException")
    override fun loadLong(key: String): Long? {
        return try {
            prefs.getLong(key, KEY_NOT_FOUND.toLong())
        } catch (throwable: Throwable) {
            null
        }
    }

    override fun remove(key: String) {
        prefs.remove(key)
    }

    companion object {
        const val KEY_NOT_FOUND = 0
    }
}
