package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLoggerCommonTest {

    fun logWithVerboseSeverity(platformDelegate: EventLoggerInterface) {
        val eventLogger = EventLogger(Severity.VERBOSE, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithDebugSeverity(platformDelegate: EventLoggerInterface) {
        val eventLogger = EventLogger(Severity.DEBUG, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithInfoSeverity(platformDelegate: EventLoggerInterface) {
        val eventLogger = EventLogger(Severity.INFO, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithWarningSeverity(platformDelegate: EventLoggerInterface) {
        val eventLogger = EventLogger(Severity.WARNING, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithErrorSeverity(platformDelegate: EventLoggerInterface) {
        val eventLogger = EventLogger(Severity.ERROR, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }
}
