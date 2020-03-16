package com.cramsan.framework.haltUtil.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.halt.implementation.HaltUtilAndroidInitializer
import com.cramsan.framework.halt.implementation.HaltUtilCommonTest
import com.cramsan.framework.halt.implementation.HaltUtilInitializer
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HaltUtilTest {

    private lateinit var haltUtilTest: HaltUtilCommonTest

    @Before
    fun setUp() {
        haltUtilTest = HaltUtilCommonTest()
    }

    @Test
    fun testStopThread() {
        runBlocking {
            val initializer = HaltUtilInitializer(HaltUtilAndroidInitializer(HaltUtilAndroid()))
            haltUtilTest.testStopThread(initializer)
        }
    }

    @Test
    fun testStopResumeStopThread() {
        runBlocking {
            val initializer = HaltUtilInitializer(HaltUtilAndroidInitializer(HaltUtilAndroid()))
            haltUtilTest.testStopResumeStopThread(initializer)
        }
    }
}
