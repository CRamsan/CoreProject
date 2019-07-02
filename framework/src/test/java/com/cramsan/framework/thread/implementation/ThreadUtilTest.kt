package com.cramsan.framework.thread.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Semaphore

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
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
        threadUtilTest.setUp()
        semaphore = Semaphore(0)
    }

    @Test
    fun testIsUIThread() {
        threadUtilTest.testIsUIThread()
    }

    @Test
    fun testIsNotBackgroundThread() {
        threadUtilTest.testIsNotBackgroundThread()
    }

    @Test
    fun testIsBackgroundThreadInJavaThread() {
        Thread { run {
            threadUtilTest.testIsBackgroundThread()
            semaphore.release()
        }}.start()
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInJavaThread() {
        Thread { run {
            threadUtilTest.testIsNotUIThread()
            semaphore.release()
        } }.start()
        semaphore.acquire()
    }

    @Test
    fun testIsBackgroundThreadInCoroutine() {
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsBackgroundThread()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInCoroutine() {
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsNotUIThread()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testIsUIThreadInDispatchToUI() {
        threadUtilTest.testIsUIThreadInDispatchToUI {
            semaphore.release()
        }
        semaphore.acquire()
    }
    
    @Test
    fun testDispatchToBackground() {
        Thread { run {
            threadUtilTest.testDispatchToBackground {
                semaphore.release()
            }
        }}.start()
        semaphore.acquire()
    }

    @Test
    fun testDispatchToBackgroundFromUIThread() {
        threadUtilTest.testDispatchToBackgroundFromUIThread {
            semaphore.release()
        }
        semaphore.acquire()
    }
}
