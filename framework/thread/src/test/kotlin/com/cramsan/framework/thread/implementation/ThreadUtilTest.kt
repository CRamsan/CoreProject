package com.cramsan.framework.thread.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Semaphore

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ThreadUtilTest {

    private lateinit var threadUtilTest: ThreadUtilCommonTest
    private lateinit var semaphore: Semaphore

    @Before
    fun setUp() {
        threadUtilTest = ThreadUtilCommonTest()
        semaphore = Semaphore(0)
    }

    @Test
    fun testIsUIThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        threadUtilTest.testIsUIThread(ThreadUtilAndroid(haltUtil))
    }

    @Test
    fun testIsNotBackgroundThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        threadUtilTest.testIsNotBackgroundThread(ThreadUtilAndroid(haltUtil))
    }

    @Test
    fun testIsBackgroundThreadInJavaThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        Thread {
            run {
                threadUtilTest.testIsBackgroundThread(ThreadUtilAndroid(haltUtil))
                semaphore.release()
            }
        }.start()
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInJavaThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        Thread {
            run {
                threadUtilTest.testIsNotUIThread(ThreadUtilAndroid(haltUtil))
                semaphore.release()
            }
        }.start()
        semaphore.acquire()
    }

    @Test
    fun testIsBackgroundThreadInCoroutine() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsBackgroundThread(ThreadUtilAndroid(haltUtil))
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInCoroutine() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsNotUIThread(ThreadUtilAndroid(haltUtil))
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testIsUIThreadInDispatchToUI() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        threadUtilTest.testIsUIThreadInDispatchToUI(ThreadUtilAndroid(haltUtil)) {
            semaphore.release()
        }
        semaphore.acquire()
    }

    @Test
    fun testDispatchToBackground() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        Thread {
            run {
                threadUtilTest.testDispatchToBackground(ThreadUtilAndroid(haltUtil)) {
                    semaphore.release()
                }
            }
        }.start()
        semaphore.acquire()
    }

    @Test
    fun testDispatchToBackgroundFromUIThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        threadUtilTest.testDispatchToBackgroundFromUIThread(ThreadUtilAndroid(haltUtil)) {
            semaphore.release()
        }
        semaphore.acquire()
    }
}
