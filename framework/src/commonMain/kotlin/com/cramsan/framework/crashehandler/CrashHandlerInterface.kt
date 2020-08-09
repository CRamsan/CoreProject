package com.cramsan.framework.crashehandler

/**
 * This module will handle crashes and report them.
 */
interface CrashHandlerInterface {

    /**
     * The delegate that will handle registering as the exception handler
     */
    val platformDelegate: CrashHandlerDelegate

    /**
     * Initialize the [platformDelegate] to start tracking crashes
     */
    fun initialize()
}
