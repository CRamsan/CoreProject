package com.cramsan.framework.logging.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Semaphore

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.kodein.di.erased.instance
import org.kodein.di.newInstance

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EventLoggerTest {

    private lateinit var eventLoggerTest: EventLoggerCommonTest

    @Before
    fun setUp() {
        eventLoggerTest = EventLoggerCommonTest()
        eventLoggerTest.setUp()
    }

    @Test
    fun logWithVerboseSeverity() {
        eventLoggerTest.logWithVerboseSeverity()
    }

    @Test
    fun logWithDebugSeverity() {
        eventLoggerTest.logWithDebugSeverity()
    }

    @Test
    fun logWithInfoSeverity() {
        eventLoggerTest.logWithInfoSeverity()
    }

    @Test
    fun logWithWarningSeverity() {
        eventLoggerTest.logWithWarningSeverity()
    }

    @Test
    fun logWithErrorSeverity() {
        eventLoggerTest.logWithErrorSeverity()
    }

    @Test
    fun assertTrueOnErrorSeverity() {
        eventLoggerTest.assertTrueOnErrorSeverity()
    }

    @Test
    fun assertFalseOnErrorSeverity() {
        eventLoggerTest.assertFalseOnErrorSeverity()
    }

    @Test
    fun assertTrueOnVerboseSeverity() {
        eventLoggerTest.assertTrueOnVerboseSeverity()
    }

    @Test
    fun assertFalseOnVerboseSeverity() {
        eventLoggerTest.assertFalseOnVerboseSeverity()
    }
}
