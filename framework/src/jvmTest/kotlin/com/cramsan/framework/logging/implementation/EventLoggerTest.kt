package com.cramsan.framework.logging.implementation

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
        eventLoggerTest.logWithVerboseSeverity(LoggerJVMInitializer(LoggerJVM()))
    }

    @Test
    fun logWithDebugSeverity() {
        eventLoggerTest.logWithDebugSeverity(LoggerJVMInitializer(LoggerJVM()))
    }

    @Test
    fun logWithInfoSeverity() {
        eventLoggerTest.logWithInfoSeverity(LoggerJVMInitializer(LoggerJVM()))
    }

    @Test
    fun logWithWarningSeverity() {
        eventLoggerTest.logWithWarningSeverity(LoggerJVMInitializer(LoggerJVM()))
    }

    @Test
    fun logWithErrorSeverity() {
        eventLoggerTest.logWithErrorSeverity(LoggerJVMInitializer(LoggerJVM()))
    }
}
