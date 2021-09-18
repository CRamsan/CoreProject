package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.logging.EventLoggerInterface
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Semaphore

/**
 */
class ThreadUtilTest {

    private lateinit var threadUtilTest: ThreadUtilCommonTest
    private lateinit var semaphore: Semaphore

    @Before
    fun setUp() {
        threadUtilTest = ThreadUtilCommonTest()
        semaphore = Semaphore(0)
    }

    @Test
    fun testIsBackgroundThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        val eventLogger = mockk<EventLoggerInterface>(relaxUnitFun = true)
        threadUtilTest.testIsBackgroundThread(ThreadUtilJVM(eventLogger, haltUtil))
    }

    @Test
    fun testIsBackgroundThreadInJavaThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        val eventLogger = mockk<EventLoggerInterface>(relaxUnitFun = true)
        Thread {
            run {
                threadUtilTest.testIsBackgroundThread(ThreadUtilJVM(eventLogger, haltUtil))
                semaphore.release()
            }
        }.start()
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInJavaThread() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        val eventLogger = mockk<EventLoggerInterface>(relaxUnitFun = true)
        Thread {
            run {
                threadUtilTest.testIsNotUIThread(ThreadUtilJVM(eventLogger, haltUtil))
                semaphore.release()
            }
        }.start()
        semaphore.acquire()
    }

    @Test
    fun testIsBackgroundThreadInCoroutine() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        val eventLogger = mockk<EventLoggerInterface>(relaxUnitFun = true)
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsBackgroundThread(ThreadUtilJVM(eventLogger, haltUtil))
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testIsNotUIThreadInCoroutine() {
        val haltUtil = mockk<AssertUtilImpl>(relaxUnitFun = true)
        val eventLogger = mockk<EventLoggerInterface>(relaxUnitFun = true)
        runBlocking {
            launch(Dispatchers.IO) {
                threadUtilTest.testIsNotUIThread(ThreadUtilJVM(eventLogger, haltUtil))
                semaphore.release()
            }
        }
        semaphore.acquire()
    }
}
