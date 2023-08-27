package com.cramsan.framework.preferences

/**
 * Platform delegate that implements the logic to store the values
 */
interface PreferencesDelegate {

    /**
     * Save [value] of type [String] as the specified [key]
     */
    fun saveString(key: String, value: String?)

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

    /**
     * Save [value] of type [Boolean] as the specified [key]
     */
    fun saveBoolean(key: String, value: Boolean)

    /**
     * Read [key] as a nullable [Boolean]
     */
    fun loadBoolean(key: String): Boolean?

    /**
     * Remove a [key] from the preferences
     */
    fun remove(key: String)
}
