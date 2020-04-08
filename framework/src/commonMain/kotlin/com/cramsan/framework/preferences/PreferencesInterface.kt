package com.cramsan.framework.preferences

interface PreferencesInterface {

    fun saveString(key: String, value: String)

    fun loadString(key: String): String?

    fun saveInt(key: String, value: Int)

    fun loadInt(key: String): Int?

    fun saveLong(key: String, value: Long)

    fun loadLong(key: String): Long?
}
