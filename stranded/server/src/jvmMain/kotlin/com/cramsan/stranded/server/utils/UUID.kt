package com.cramsan.stranded.server.utils

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
