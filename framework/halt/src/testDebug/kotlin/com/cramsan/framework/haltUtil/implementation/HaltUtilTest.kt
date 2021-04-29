package com.cramsan.framework.haltUtil.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.halt.implementation.HaltUtilCommonTest
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

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var haltUtilTest: HaltUtilCommonTest

    @Before
    fun setUp() {
        haltUtilTest = HaltUtilCommonTest()
    }

    @Test
    fun testStopThread() {
        runBlocking {
            haltUtilTest.testStopThread(HaltUtilAndroid(context))
        }
    }

    @Test
    fun testStopResumeStopThread() {
        runBlocking {
            haltUtilTest.testStopResumeStopThread(HaltUtilAndroid(context))
        }
    }
}
