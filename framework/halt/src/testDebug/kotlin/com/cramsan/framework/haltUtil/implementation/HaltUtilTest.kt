package com.cramsan.framework.haltUtil.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.test.TestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import kotlin.concurrent.thread
import kotlin.test.Test

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HaltUtilTest : TestBase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    override fun setupTest() {
    }

    @Test
    fun testStopThread() = runBlockingTest {
        val haltUtil = HaltUtilImpl(HaltUtilAndroid(context))

        thread {
            Thread.sleep(1500)
            haltUtil.resumeThread()
        }
        haltUtil.stopThread()
    }
}
