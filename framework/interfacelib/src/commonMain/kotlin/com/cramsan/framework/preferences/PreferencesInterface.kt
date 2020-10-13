package com.cramsan.framework.preferences

/**
 * This module has a simple API to easily persist data in local storage. It was designed to be used for simple operations that do not require high performance.
 */
interface PreferencesInterface {

    val platformDelegate: PreferencesDelegate

    /**
     * Save [value] of type [String] as the specified [key]
     */
    fun saveString(key: String, value: String)

    /**
     * Read [key] as a nullable [String]
     */
    fun loadString(key: String): String?

    /**
     * Save [value] of type [Int] as the specified [key]
     */
    fun saveInt(key: String, value: Int)

    /**
     * Read [key] as a nullable [Int]
     */
    fun loadInt(key: String): Int?

    /**
     * Save [value] of type [Long] as the specified [key]
     */
    fun saveLong(key: String, value: Long)

    /**
     * Read [key] as a nullable [Long]
     */
    fun loadLong(key: String): Long?
}
