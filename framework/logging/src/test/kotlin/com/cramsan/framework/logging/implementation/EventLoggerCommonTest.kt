package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logV
import com.cramsan.framework.logging.logW
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class EventLoggerCommonTest : TestBase() {

    private lateinit var platformDelegate: EventLoggerDelegate

    override fun setupTest() {
        platformDelegate = mockk(relaxUnitFun = true)
    }

    @Test
    fun logWithVerboseSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallback>(relaxUnitFun = true)
        val eventLogger = EventLoggerImpl(Severity.VERBOSE, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithDebugSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallback>(relaxUnitFun = true)
        val eventLogger = EventLoggerImpl(Severity.DEBUG, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithInfoSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallback>(relaxUnitFun = true)
        val eventLogger = EventLoggerImpl(Severity.INFO, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithWarningSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallback>(relaxUnitFun = true)
        val eventLogger = EventLoggerImpl(Severity.WARNING, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun logWithErrorSeverity() = runBlockingTest {
        val errorCallback = mockk<EventLoggerErrorCallback>(relaxUnitFun = true)
        val eventLogger = EventLoggerImpl(Severity.ERROR, errorCallback, platformDelegate)
        eventLogger.log(Severity.VERBOSE, "Test", "Message-1")
        eventLogger.log(Severity.DEBUG, "Test", "Message-2")
        eventLogger.log(Severity.INFO, "Test", "Message-3")
        eventLogger.log(Severity.WARNING, "Test", "Message-4")
        eventLogger.log(Severity.ERROR, "Test", "Message-5")
    }

    @Test
    fun `test logV top-level function`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure singleton
        com.cramsan.framework.logging.EventLogger.setInstance(eventLogger)

        logV(tag, message)
        verify { eventLogger.v(tag, message) }
    }

    @Test
    fun `test logD top-level function`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure singleton
        com.cramsan.framework.logging.EventLogger.setInstance(eventLogger)

        logD(tag, message)
        verify { eventLogger.d(tag, message) }
    }

    @Test
    fun `test logI top-level function`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure singleton
        com.cramsan.framework.logging.EventLogger.setInstance(eventLogger)

        logI(tag, message)
        verify { eventLogger.i(tag, message) }
    }

    @Test
    fun `test logW top-level functions`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)
        val warningException: Throwable = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure singleton
        com.cramsan.framework.logging.EventLogger.setInstance(eventLogger)

        logW(tag, message)
        verify { eventLogger.w(tag, message, null) }

        logW(tag, message, warningException)
        verify { eventLogger.w(tag, message, warningException) }
    }

    @Test
    fun `test logE top-level functions`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)
        val errorException: Throwable = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure singleton
        com.cramsan.framework.logging.EventLogger.setInstance(eventLogger)

        logE(tag, message)
        verify { eventLogger.e(tag, message, null) }

        logE(tag, message, errorException)
        verify { eventLogger.e(tag, message, errorException) }
    }

    @Test
    fun `test configuring singleton`() = runBlockingTest {
        val eventLogger: EventLoggerInterface = mockk(relaxed = true)

        // Configure singleton
        EventLogger.setInstance(eventLogger)

        // Configure the singleton
        assertEquals(eventLogger, com.cramsan.framework.logging.EventLogger.singleton)
    }
}
