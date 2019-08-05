package com.cramsan.framework.haltUtil.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.halt.implementation.HaltUtilCommonTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HaltUtilTest {

    private lateinit var haltUtilTest: HaltUtilCommonTest

    @Before
    fun setUp() {
        haltUtilTest = HaltUtilCommonTest()
        haltUtilTest.setUp()
    }

    @Test
    fun testStopThread() {
        runBlocking {
            haltUtilTest.testStopThread()
        }
    }

    @Test
    fun testStopMainThread() {
        haltUtilTest.testStopMainThread()
    }

    @Test
    fun testCrashApp() {
        haltUtilTest.testCrashApp()
    }
}
