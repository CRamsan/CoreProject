package com.cramsan.framework.preferences.implementation

import android.content.Context
import android.content.SharedPreferences
import com.cramsan.framework.preferences.PreferencesDelegate

/**
 * This implementation of [PreferencesDelegate] uses the [context] to manage a [SharedPreferences].
 *
 * TODO: Remove the hardcoded file name. Preferably make it injectable.
 * TODO: Keep a reference to the [SharedPreferences] after initialization to prevent excessive IO.
 */
class PreferencesAndroid(context: Context) : PreferencesDelegate {

    private val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)

    override fun saveString(key: String, value: String?) {
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    override fun loadString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    override fun saveInt(key: String, value: Int) {
        with(sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }

    override fun loadInt(key: String): Int? {
        return sharedPref.getInt(key, 0)
    }

    override fun saveLong(key: String, value: Long) {
        with(sharedPref.edit()) {
            putLong(key, value)
            commit()
        }
    }

    override fun loadLong(key: String): Long {
        return sharedPref.getLong(key, 0)
    }

    override fun remove(key: String) {
        with(sharedPref.edit()) {
            remove(key)
            commit()
        }
    }
}
