package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerPlatformInitializerInterface
import com.cramsan.framework.logging.Severity


class EventLoggerCommonTest {

    fun logWithVerboseSeverity(platformInitializer: EventLoggerPlatformInitializerInterface) {
        val eventLogger = EventLogger(EventLoggerInitializer(platformInitializer, Severity.VERBOSE))
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithDebugSeverity(platformInitializer: EventLoggerPlatformInitializerInterface) {
        val eventLogger = EventLogger(EventLoggerInitializer(platformInitializer, Severity.DEBUG))
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithInfoSeverity(platformInitializer: EventLoggerPlatformInitializerInterface) {
        val eventLogger = EventLogger(EventLoggerInitializer(platformInitializer, Severity.INFO))
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithWarningSeverity(platformInitializer: EventLoggerPlatformInitializerInterface) {
        val eventLogger = EventLogger(EventLoggerInitializer(platformInitializer, Severity.WARNING))
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    fun logWithErrorSeverity(platformInitializer: EventLoggerPlatformInitializerInterface) {
        val eventLogger = EventLogger(EventLoggerInitializer(platformInitializer, Severity.ERROR))
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }
}
