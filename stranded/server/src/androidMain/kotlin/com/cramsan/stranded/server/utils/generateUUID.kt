package com.cramsan.stranded.server.utils

import java.util.UUID

/**
 * Generate a UUID string.
 */
actual fun generateUUID(): String = UUID.randomUUID().toString()