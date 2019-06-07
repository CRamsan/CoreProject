package com.cramsan.petproject.logging.implementation

import com.cramsan.petproject.logging.PlatformLoggerInterface
import com.cramsan.petproject.logging.Severity

actual class PlatformLogger : PlatformLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
    }
}