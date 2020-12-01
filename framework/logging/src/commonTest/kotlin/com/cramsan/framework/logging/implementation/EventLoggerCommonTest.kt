package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test

class EventLoggerCommonTest : TestBase() {

    private lateinit var platformDelegate: EventLoggerDelegate

    @BeforeTest
    fun setUp() {
        platformDelegate = mockk(relaxUnitFun = true)
    }

    @Test
    fun logWithVerboseSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        val eventLogger = EventLogger(Severity.VERBOSE, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithDebugSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        val eventLogger = EventLogger(Severity.DEBUG, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithInfoSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        val eventLogger = EventLogger(Severity.INFO, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithWarningSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        val eventLogger = EventLogger(Severity.WARNING, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithErrorSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        val eventLogger = EventLogger(Severity.ERROR, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }
}
