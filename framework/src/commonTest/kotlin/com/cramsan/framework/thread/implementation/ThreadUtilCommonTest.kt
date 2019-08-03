package com.cramsan.framework.thread.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import io.mockk.every
import io.mockk.mockk
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadUtilCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>(relaxUnitFun = true) }
    }

    private lateinit var threadUtil: ThreadUtilInterface

    fun setUp() {
        val eventLoggerInterface: EventLoggerInterface by kodein.instance()

        val initializer = ThreadUtilInitializer(eventLoggerInterface)
        threadUtil = ThreadUtil(initializer)
    }

    fun testIsUIThread() {
        assertTrue(threadUtil.isUIThread())
    }

    fun testIsNotUIThread() {
        assertFalse(threadUtil.isUIThread())
    }

    fun testIsBackgroundThread() {
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testIsNotBackgroundThread() {
        assertFalse(threadUtil.isBackgroundThread())
    }

    fun testIsUIThreadInDispatchToUI(completion :() -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToUI {
            assertTrue(threadUtil.isUIThread())
            completion()
        }
    }

    fun testDispatchToBackground(completion: () -> Unit) {
        assertTrue(threadUtil.isBackgroundThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testDispatchToBackgroundFromUIThread(completion: () -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isUIThread())
    }
}
