package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.crashehandler.CrashHandlerDelegate

/**
 *  Module implementation that delegates the initialization to the [platformDelegate]
 */
class CrashHandlerImpl(override val platformDelegate: CrashHandlerDelegate) : CrashHandler {
    override fun initialize() {
        platformDelegate.initialize()
    }
}
