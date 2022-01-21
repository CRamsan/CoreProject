package com.cramsan.framework.thread

/**
 * Module with utility functions around threads and dispatching blocks
 */
interface ThreadUtilInterface {

    /**
     * [ThreadUtilDelegate] that will be used for this instance of [ThreadUtilInterface].
     */
    val platformDelegate: ThreadUtilDelegate

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
