package com.cramsan.framework.thread.implementation

import com.cramsan.framework.logging.implementation.MockEventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadUtilCommonTest {

    private lateinit var threadUtil: ThreadUtilInterface

    fun setUp() {
        val initializer = ThreadUtilInitializer(MockEventLogger())
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
