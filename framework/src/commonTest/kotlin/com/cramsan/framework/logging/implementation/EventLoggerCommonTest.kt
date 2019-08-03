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

    fun setUp() {

    }

    fun logWithVerboseSeverity() {
        val initializer = EventLoggerInitializer(Severity.VERBOSE)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithDebugSeverity() {
        val initializer = EventLoggerInitializer(Severity.DEBUG)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithInfoSeverity() {
        val initializer = EventLoggerInitializer(Severity.INFO)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithWarningSeverity() {
        val initializer = EventLoggerInitializer(Severity.WARNING)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun logWithErrorSeverity() {
        val initializer = EventLoggerInitializer(Severity.ERROR)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.log(Severity.VERBOSE, classTag(), "Message-1")
        eventLogger.log(Severity.DEBUG, classTag(), "Message-2")
        eventLogger.log(Severity.INFO, classTag(), "Message-3")
        eventLogger.log(Severity.WARNING, classTag(), "Message-4")
        eventLogger.log(Severity.ERROR, classTag(), "Message-5")
    }

    fun assertTrueOnErrorSeverity() {
        val initializer = EventLoggerInitializer(Severity.ERROR)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }

    fun assertFalseOnErrorSeverity() {
        val initializer = EventLoggerInitializer(Severity.ERROR)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.assert(false, classTag(), "Assert-Message-1")
    }

    fun assertTrueOnVerboseSeverity() {
        val initializer = EventLoggerInitializer(Severity.VERBOSE)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }

    fun assertFalseOnVerboseSeverity() {
        val initializer = EventLoggerInitializer(Severity.VERBOSE)
        val eventLogger by kodein.newInstance { EventLogger(initializer, instance()) }
        eventLogger.assert(true, classTag(), "Assert-Message-1")
    }
}