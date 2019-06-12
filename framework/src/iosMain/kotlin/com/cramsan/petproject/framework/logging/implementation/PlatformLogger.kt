package com.cramsan.petproject.framework.logging.implementation

import com.cramsan.petproject.framework.logging.PlatformLoggerInterface
import com.cramsan.petproject.framework.logging.Severity

internal actual class PlatformLogger : PlatformLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
    }
}