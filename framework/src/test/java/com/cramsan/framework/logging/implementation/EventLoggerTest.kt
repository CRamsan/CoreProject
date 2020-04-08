package com.cramsan.framework.logging.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EventLoggerTest {

    private lateinit var eventLoggerTest: EventLoggerCommonTest

    @Before
    fun setUp() {
        eventLoggerTest = EventLoggerCommonTest()
    }

    @Test
    fun logWithVerboseSeverity() {
        eventLoggerTest.logWithVerboseSeverity(LoggerAndroid())
    }

    @Test
    fun logWithDebugSeverity() {
        eventLoggerTest.logWithDebugSeverity(LoggerAndroid())
    }

    @Test
    fun logWithInfoSeverity() {
        eventLoggerTest.logWithInfoSeverity(LoggerAndroid())
    }

    @Test
    fun logWithWarningSeverity() {
        eventLoggerTest.logWithWarningSeverity(LoggerAndroid())
    }

    @Test
    fun logWithErrorSeverity() {
        eventLoggerTest.logWithErrorSeverity(LoggerAndroid())
    }
}
