package com.cramsan.framework.halt

/**
 * Module interface to manage threads for debug purposes. This module will delegate to [platformDelegate]
 * to accomplish this based on the requirements of each platform.
 *
 * @see [HaltUtilDelegate]
 */
interface HaltUtil {

    /**
     * Platform delegate
     */
    val platformDelegate: HaltUtilDelegate

    /**
     * If the current thread is halted, resume it.
     *
     * @see [HaltUtilDelegate.resumeThread]
     */
    fun resumeThread()

    /**
     * Halt the current thread.
     *
     * @see [HaltUtilDelegate.stopThread]
     */
    fun stopThread()

    /**
     * Crash the process
     *
     * @see [HaltUtilDelegate.crashApp]
     */
    fun crashApp()
}
