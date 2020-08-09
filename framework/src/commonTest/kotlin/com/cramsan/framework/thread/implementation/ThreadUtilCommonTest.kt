package com.cramsan.framework.thread.implementation

import com.cramsan.framework.thread.ThreadUtilDelegate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadUtilCommonTest {

    fun testIsUIThread(threadUtil: ThreadUtilDelegate) {
        assertTrue(threadUtil.isUIThread())
    }

    fun testIsNotUIThread(threadUtil: ThreadUtilDelegate) {
        assertFalse(threadUtil.isUIThread())
    }

    fun testIsBackgroundThread(threadUtil: ThreadUtilDelegate) {
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testIsNotBackgroundThread(threadUtil: ThreadUtilDelegate) {
        assertFalse(threadUtil.isBackgroundThread())
    }

    fun testIsUIThreadInDispatchToUI(threadUtil: ThreadUtilDelegate, completion: () -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToUI {
            assertTrue(threadUtil.isUIThread())
            completion()
        }
    }

    fun testDispatchToBackground(threadUtil: ThreadUtilDelegate, completion: () -> Unit) {
        assertTrue(threadUtil.isBackgroundThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isBackgroundThread())
    }

    fun testDispatchToBackgroundFromUIThread(threadUtil: ThreadUtilDelegate, completion: () -> Unit) {
        assertTrue(threadUtil.isUIThread())
        threadUtil.dispatchToBackground {
            assertTrue(threadUtil.isBackgroundThread())
            completion()
        }
        assertTrue(threadUtil.isUIThread())
    }
}
