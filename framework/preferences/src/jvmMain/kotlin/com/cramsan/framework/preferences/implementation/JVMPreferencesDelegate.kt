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
        return verifyAndGetOrNull(key) {
            prefs.get(key, null)
        }
    }

    override fun saveInt(key: String, value: Int) {
        prefs.putInt(key, value)
    }

    override fun loadInt(key: String): Int? {
        return verifyAndGetOrNull(key) {
            prefs.getInt(key, Int.MIN_VALUE)
        }
    }

    override fun saveLong(key: String, value: Long) {
        prefs.putLong(key, value)
    }

    override fun loadLong(key: String): Long? {
        return verifyAndGetOrNull(key) {
            prefs.getLong(key, Long.MIN_VALUE)
        }
    }

    override fun saveBoolean(key: String, value: Boolean) {
        prefs.putBoolean(key, value)
    }

    override fun loadBoolean(key: String): Boolean? {
        return verifyAndGetOrNull(key) {
            prefs.getBoolean(key, false)
        }
    }

    @Suppress("SwallowedException")
    private fun <T> verifyAndGetOrNull(
        nodeName: String,
        block: (nodeName: String) -> T?
    ): T? {
        return try {
            // TODO: Verify that the key exists
            block(nodeName)
        } catch (throwable: Throwable) {
            null
        }
    }

    override fun remove(key: String) {
        prefs.remove(key)
    }
}
