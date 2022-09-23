package com.cramsan.framework.utils.uuid

/**
 * Simple UUID generator.
 */
expect object UUID {

    /**
     * Generates a random UUID based on a platform specific implementation.
     */
    fun random(): String

    /**
     * Generates a UUID based on the byte content of the provided [input].
     */
    fun fromString(input: String): String
}
