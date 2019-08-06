package com.cramsan.framework.thread.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

class ThreadUtilCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>(relaxUnitFun = true) }
    }

    private lateinit var threadUtil: ThreadUtilInterface

    fun setUp() {
        val newThreadUtil by kodein.newInstance { ThreadUtil(instance()) }
        threadUtil = newThreadUtil
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

    fun testIsUIThreadInDispatchToUI(completion: () -> Unit) {
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
