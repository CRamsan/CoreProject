package com.cramsan.stranded.server.utils

/**
 * Generate a UUID string.
 *
 * On the JS platform we do not have native capabilities to create UUIDs.
 */
actual fun generateUUID(): String = ""