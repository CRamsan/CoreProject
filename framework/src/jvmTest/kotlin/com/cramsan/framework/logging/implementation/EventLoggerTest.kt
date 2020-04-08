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
        eventLoggerTest.logWithVerboseSeverity(LoggerJVM())
    }

    @Test
    fun logWithDebugSeverity() {
        eventLoggerTest.logWithDebugSeverity(LoggerJVM())
    }

    @Test
    fun logWithInfoSeverity() {
        eventLoggerTest.logWithInfoSeverity(LoggerJVM())
    }

    @Test
    fun logWithWarningSeverity() {
        eventLoggerTest.logWithWarningSeverity(LoggerJVM())
    }

    @Test
    fun logWithErrorSeverity() {
        eventLoggerTest.logWithErrorSeverity(LoggerJVM())
    }
}
