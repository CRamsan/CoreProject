package com.cramsan.framework.thread

/**
 * Delegate that will need to implement the platform logic to manage
 * threading.
 */
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
     * Assert that the current thread is the UI thread
     */
    fun assertIsUIThread()

    /**
     * Assert that the current thread is a background thread
     */
    fun assertIsBackgroundThread()
}
