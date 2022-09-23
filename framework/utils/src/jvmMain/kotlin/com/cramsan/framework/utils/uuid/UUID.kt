package com.cramsan.framework.utils.uuid

import java.util.UUID

/**
 * Simple UUID generator.
 */
actual object UUID {

    /**
     * Generates a random UUID based on a platform specific implementation.
     */
    actual fun random(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Generates a UUID based on the byte content of the provided [input].
     */
    actual fun fromString(input: String): String {
        return UUID.nameUUIDFromBytes(input.toByteArray()).toString()
    }
}
