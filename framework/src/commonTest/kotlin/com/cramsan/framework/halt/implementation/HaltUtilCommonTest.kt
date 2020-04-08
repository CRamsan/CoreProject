package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HaltUtilCommonTest {

    suspend fun testStopThread(platformDelegate: HaltUtilInterface) = coroutineScope {
        val haltUtil = HaltUtil(platformDelegate)

        launch(Dispatchers.Default) {
            delay(DELAY_TIME)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
    }

    suspend fun testStopResumeStopThread(platformDelegate: HaltUtilInterface) = coroutineScope {
        val haltUtil = HaltUtil(platformDelegate)

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
