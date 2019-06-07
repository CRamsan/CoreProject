package com.cramsan.petproject.logging.implementation

import com.cramsan.petproject.logging.EventLoggerInterface
import com.cramsan.petproject.logging.PlatformLoggerInterface
import com.cramsan.petproject.logging.Severity

class EventLogger(private val platformLogger: PlatformLoggerInterface): EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String) {
        platformLogger.log(severity, tag, message)
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (!condition)
            platformLogger.log(Severity.ERROR, tag, message)
    }
}