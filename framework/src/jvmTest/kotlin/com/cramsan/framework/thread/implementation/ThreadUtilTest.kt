package com.cramsan.framework.thread.implementation

import java.util.concurrent.Semaphore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 */
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
    fun testIsBackgroundThread() {
        threadUtilTest.testIsBackgroundThread()
    }

    @Test
    fun testIsBackgroundThreadInJavaThread() {
        Thread { run {
            threadUtilTest.testIsBackgroundThread()
            semaphore.release()
        } }.start()
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
    fun testDispatchToBackground() {
        Thread { run {
            threadUtilTest.testDispatchToBackground {
                semaphore.release()
            }
        } }.start()
        semaphore.acquire()
    }
}
