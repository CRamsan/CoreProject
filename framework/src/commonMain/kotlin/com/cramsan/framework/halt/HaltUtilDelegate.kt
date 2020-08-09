package com.cramsan.framework.halt

/**
 * Interface for a specific implementation of the halt mechanism
 */
interface HaltUtilDelegate {
    fun resumeThread()
    fun stopThread()
    fun crashApp()
}
