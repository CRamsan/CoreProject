package com.cramsan.framework.haltUtil.implementation

import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.assertTrue

/**
 * This code is duplicated for Android and JVM. This is due to the plugin not supporting sharing
 * across JVM+Android.
 * https://kotlinlang.org/docs/mpp-share-on-platforms.html#share-code-in-libraries
 */
class HaltUtilCommonTest {

    suspend fun testStopThread(platformDelegate: HaltUtilDelegate) = coroutineScope {
        val haltUtil = HaltUtilImpl(platformDelegate)

        launch(Dispatchers.Default) {
            delay(DELAY_TIME)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
    }

    suspend fun testStopResumeStopThread(platformDelegate: HaltUtilDelegate) = coroutineScope {
        val haltUtil = HaltUtilImpl(platformDelegate)

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
        private const val DELAY_TIME = 100L
    }
}
