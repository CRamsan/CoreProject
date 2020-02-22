package com.cramsan.framework.halt.implementation

import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.assert.implementation.AssertUtilInitializer
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.assertTrue

class HaltUtilCommonTest {

    suspend fun testStopThread() = coroutineScope {
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = HaltUtil(eventLogger)

        launch(Dispatchers.Default) {
            delay(DELAY_TIME)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
    }

    suspend fun testStopResumeStopThread() = coroutineScope {
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = HaltUtil(eventLogger)

        launch(Dispatchers.Default) {
            delay(DELAY_TIME)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
        var didWait = false
        launch(Dispatchers.Default) {
            delay(DELAY_TIME)
            haltUtil.resumeThread()
            didWait = true
        }
        haltUtil.stopThread()
        assertTrue(didWait)
    }

    companion object {
        private const val DELAY_TIME = 2000L
    }
}
