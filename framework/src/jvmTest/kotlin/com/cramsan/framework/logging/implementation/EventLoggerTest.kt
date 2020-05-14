package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 */
class EventLoggerTest {

    private lateinit var eventLoggerTest: EventLoggerCommonTest

    @Before
    fun setUp() {
        eventLoggerTest = EventLoggerCommonTest()
    }

    @Test
    fun logWithVerboseSeverity() {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        eventLoggerTest.logWithVerboseSeverity(LoggerJVM(), errorCallback)
    }

    @Test
    fun logWithDebugSeverity() {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        eventLoggerTest.logWithDebugSeverity(LoggerJVM(), errorCallback)
    }

    @Test
    fun logWithInfoSeverity() {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        eventLoggerTest.logWithInfoSeverity(LoggerJVM(), errorCallback)
    }

    @Test
    fun logWithWarningSeverity() {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        eventLoggerTest.logWithWarningSeverity(LoggerJVM(), errorCallback)
    }

    @Test
    fun logWithErrorSeverity() {
        val errorCallback = mockk<EventLoggerErrorCallbackInterface>(relaxUnitFun = true)
        eventLoggerTest.logWithErrorSeverity(LoggerJVM(), errorCallback)
    }
}
