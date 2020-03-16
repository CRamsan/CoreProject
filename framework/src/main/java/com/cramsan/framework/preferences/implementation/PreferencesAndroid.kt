package com.cramsan.framework.preferences.implementation

import android.content.Context
import com.cramsan.framework.preferences.PreferencesInterface

class PreferencesAndroid(private val applicationContext: Context) : PreferencesInterface {

    override fun saveString(key: String, value: String) {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    override fun loadString(key: String): String? {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")
    }

    override fun saveInt(key: String, value: Int) {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }

    override fun loadInt(key: String): Int? {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getInt(key, 0)
    }

    override fun saveLong(key: String, value: Long) {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong(key, value)
            commit()
        }
    }

    override fun loadLong(key: String): Long? {
        val sharedPref = applicationContext.getSharedPreferences("global", Context.MODE_PRIVATE)
        return sharedPref.getLong(key, 0)
    }
}
