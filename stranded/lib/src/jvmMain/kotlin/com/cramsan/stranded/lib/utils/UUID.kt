package com.cramsan.stranded.lib.utils

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
