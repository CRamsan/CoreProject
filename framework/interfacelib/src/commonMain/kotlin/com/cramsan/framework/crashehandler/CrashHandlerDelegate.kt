package com.cramsan.framework.crashehandler

/**
 * Delegate to provide a specific implementation for the crash handler.
 *
 * @see [CrashHandler]
 */
interface CrashHandlerDelegate {

    /**
     * Initialize this delegate. This function will be called very early in the process's lifecycle.
     *
     * @see [CrashHandler.initialize]
     */
    fun initialize()
}
