package com.cramsan.framework.haltUtil.implementation

import com.cramsan.framework.halt.implementation.HaltUtilJVM
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 */
class HaltUtilTest {

    private lateinit var haltUtilTest: HaltUtilCommonTest

    @Before
    fun setUp() {
        haltUtilTest = HaltUtilCommonTest()
    }

    // https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // We cannot use the runBlockingTest context due to this issue.
    @Test
    fun testStopThread() {
        runBlocking {
            haltUtilTest.testStopThread(HaltUtilJVM())
        }
    }
}
