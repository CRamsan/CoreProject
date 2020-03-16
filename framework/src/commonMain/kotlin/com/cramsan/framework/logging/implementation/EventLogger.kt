package com.cramsan.framework.logging.implementation

import com.cramsan.framework.base.implementation.BaseModule
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLogger(initializer: EventLoggerInitializer) : BaseModule<LoggerManifest>(initializer), EventLoggerInterface {

    private val targetSeverity = initializer.targetSeverity
    private val platformLogger = initializer.platformInitializer.platformLogger

    override fun log(severity: Severity, tag: String, message: String) {
        if (severity < targetSeverity)
            return
        platformLogger.log(severity, tag, message)
    }
}
