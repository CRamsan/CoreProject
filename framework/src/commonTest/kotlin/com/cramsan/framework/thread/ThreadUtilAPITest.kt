package com.cramsan.framework.thread

import com.cramsan.framework.logging.MockEventLogger
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadUtilAPITest {

    private lateinit var threadUtil: ThreadUtilInterface

    @BeforeTest
    fun setUp() {
        val initializer = ThreadUtilInitializer(MockEventLogger())
        ThreadUtilAPI.init(initializer)
        threadUtil = ThreadUtilAPI.threadUtil
    }

    @Test
    fun testIsUIThread() {
        assertTrue(threadUtil.isUIThread())
    }

    @Test
    fun testIsUIThreadInDispatchToUI() {
        threadUtil.dispatchToUI {
            assertTrue(threadUtil.isUIThread())
        }
    }

    @Test
    fun testIsUIThreadInDispatchToBackGroundThreadAndBack() {
        threadUtil.dispatchToBackground {
            threadUtil.dispatchToUI {
                assertTrue(threadUtil.isUIThread())
            }
        }
    }

    @Test
    fun dispatchToBackground() {
        threadUtil.dispatchToBackground {
            assertFalse(threadUtil.isUIThread())
            assertTrue(threadUtil.isBackgroundThread())
        }
    }

    @Test
    fun dispatchToUI() {
        assertTrue(threadUtil.isUIThread())
        assertFalse(threadUtil.isBackgroundThread())
        threadUtil.dispatchToUI {
            assertTrue(threadUtil.isUIThread())
            assertFalse(threadUtil.isBackgroundThread())
        }
    }
}