package com.cramsan.framework.crashehandler

/**
 * This module will handle crashes and report them. By convention, the implementation will take on
 * the complexity of catching the crashes and report them as appropriate. The caller only will need
 * to provide a [platformDelegate] and then ensure to call [initialize] as early as possible.
 *
 * @see [CrashHandlerDelegate]
 */
interface CrashHandler {

    /**
     * The delegate that will catch the unhandled exceptions.
     *
     * @see [CrashHandlerDelegate]
     */
    val platformDelegate: CrashHandlerDelegate

    /**
     * Initialize the [platformDelegate] to start tracking crashes.
     *
     * @see [CrashHandlerDelegate.initialize]
     */
    fun initialize()
}
