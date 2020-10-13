package com.cramsan.framework.thread

interface ThreadUtilDelegate {
    /**
     * Return true is the current thread is the UI thread
     */
    fun isUIThread(): Boolean

    /**
     * Return true if the current thread it a non-UI thread
     */
    fun isBackgroundThread(): Boolean

    /**
     * The [block] will be dispatched to be executed in a background thread
     */
    fun dispatchToBackground(block: RunBlock)

    /**
     * The [block] will be dispatched to be executed in the UI thread
     */
    fun dispatchToUI(block: RunBlock)

    /**
     * Assert that the current thread is the UI thread
     */
    fun assertIsUIThread()

    /**
     * Assert that the current thread is a background thread
     */
    fun assertIsBackgroundThread()
}
