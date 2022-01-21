package com.cramsan.framework.utils.format

/**
 * Simple String formatter.
 */
expect class StringFormatter() {

    /**
     * Format the [string] using the provided [args]. This [string] is expected to match Java's version
     * of a string.
     */
    fun format(string: String, vararg args: Any?): String
}
