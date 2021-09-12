package com.cramsan.framework.halt

/**
 * Interface for a specific implementation of the halt mechanism.
 *
 * @see [HaltUtil]
 */
interface HaltUtilDelegate {

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
