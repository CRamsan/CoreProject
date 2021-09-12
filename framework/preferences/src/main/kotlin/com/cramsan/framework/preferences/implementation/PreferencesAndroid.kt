package com.cramsan.framework.preferences.implementation

import android.content.Context
import android.content.SharedPreferences
import com.cramsan.framework.preferences.PreferencesDelegate

/**
 * This implementation of [PreferencesDelegate] uses the [context] to manage a [SharedPreferences]
 *
 * TODO: Remove the hardcoded file name. Preferably make it injectable.
 * TODO: Keep a reference to the [SharedPreferences] after initialization to prevent excessive IO.
 */
class PreferencesAndroid(private val context: Context) : PreferencesDelegate {

    override fun saveString(key: String, value: String?) {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    override fun loadString(key: String): String? {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    override fun saveInt(key: String, value: Int?) {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            if (value == null) {
                remove(key)
            } else {
                putInt(key, value)
            }
            commit()
        }
    }

    override fun loadInt(key: String): Int? {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getInt(key, 0)
    }

    override fun saveLong(key: String, value: Long?) {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            if (value == null) {
                remove(key)
            } else {
                putLong(key, value)
            }
            commit()
        }
    }

    override fun loadLong(key: String): Long? {
        val sharedPref = context.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getLong(key, 0)
    }
}
