package com.cramsan.framework.halt

/**
 * Module interface to manage threads for debug purposes. This module will delegate to [platformDelegate]
 * to accomplish this based on the requirements of each platform.
 */
interface HaltUtil {

    /**
     * Platform delegate
     */
    val platformDelegate: HaltUtilDelegate

    /**
     * If the current thread is halted, resume it.
     */
    fun resumeThread()

    /**
     * Halt the current thread.
     */
    fun stopThread()

    /**
     * Crash the process
     */
    fun crashApp()
}
