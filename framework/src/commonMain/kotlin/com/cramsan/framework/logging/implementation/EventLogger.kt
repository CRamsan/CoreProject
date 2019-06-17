package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.PlatformLoggerInterface
import com.cramsan.framework.logging.Severity

internal class EventLogger(private val platformLogger: PlatformLoggerInterface): EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String) {
        platformLogger.log(severity, tag, message)
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (!condition)
            platformLogger.log(Severity.ERROR, tag, message)
    }
}