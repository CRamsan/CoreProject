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
}
