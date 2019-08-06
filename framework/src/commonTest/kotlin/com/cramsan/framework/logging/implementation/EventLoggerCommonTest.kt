package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.PlatformLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import io.mockk.mockk
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

class EventLoggerCommonTest {

    private val kodein = Kodein {
        bind<PlatformLoggerInterface>() with provider { mockk<PlatformLogger>(relaxUnitFun = true) }
    }

    fun logWithVerboseSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.VERBOSE, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithDebugSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.DEBUG, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithInfoSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.INFO, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithWarningSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.WARNING, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithErrorSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.ERROR, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun assertTrueOnErrorSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.ERROR, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }

    fun assertFalseOnErrorSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.ERROR, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.assert(false, classTag(), "Assert-Message-1")
    }

    fun assertTrueOnVerboseSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.VERBOSE, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }

    fun assertFalseOnVerboseSeverity() {
        val initializer by kodein.newInstance { EventLoggerInitializer(Severity.VERBOSE, instance()) }
        val eventLogger = EventLogger(initializer)
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }
}
