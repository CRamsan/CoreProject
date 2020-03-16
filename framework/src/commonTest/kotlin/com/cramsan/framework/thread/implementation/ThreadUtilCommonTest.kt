package com.cramsan.framework.thread.implementation

import com.cramsan.framework.thread.ThreadUtilInterface
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadUtilCommonTest {

    fun testIsUIThread(threadUtil: ThreadUtilInterface) {
        assertTrue(threadUtil.isUIThread())
    }

    fun testIsNotUIThread(threadUtil: ThreadUtilInterface) {
        assertFalse(threadUtil.isUIThread())
    }

    fun testIsBackgroundThread(threadUtil: ThreadUtilInterface) {
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testIsNotBackgroundThread(threadUtil: ThreadUtilInterface) {
        assertFalse(threadUtil.isBackgroundThread())
    }

    fun testIsUIThreadInDispatchToUI(threadUtil: ThreadUtilInterface, completion: () -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToUI {
            assertTrue(threadUtil.isUIThread())
            completion()
        }
    }

    fun testDispatchToBackground(threadUtil: ThreadUtilInterface, completion: () -> Unit) {
        assertTrue(threadUtil.isBackgroundThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testDispatchToBackgroundFromUIThread(threadUtil: ThreadUtilInterface, completion: () -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isUIThread())
    }
}
