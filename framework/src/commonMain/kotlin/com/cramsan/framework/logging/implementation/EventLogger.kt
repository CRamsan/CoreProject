package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.PlatformLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLogger(initializer: EventLoggerInitializer) : EventLoggerInterface {

    private val targetSeverity: Severity = initializer.targetSeverity
    private val platformLogger: PlatformLoggerInterface = initializer.platformLogger

    override fun log(severity: Severity, tag: String, message: String) {
        if (severity < targetSeverity)
            return
        platformLogger.log(severity, tag, message)
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (condition) {
            return
        }
        platformLogger.log(Severity.ERROR, tag, message)
    }
}
