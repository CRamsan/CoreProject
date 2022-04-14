package com.cramsan.framework.haltUtil.implementation

import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.test.TestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.concurrent.thread
import kotlin.test.Test

/**
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HaltUtilTest : TestBase() {

    override fun setupTest() { }

    @Test
    fun testStopThread() = runBlockingTest {
        val haltUtil = HaltUtilImpl(HaltUtilJVM())

        thread {
            Thread.sleep(1500)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
    }
}
