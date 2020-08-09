package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.cramsan.framework.crashehandler.CrashHandlerInterface

/**
 *  Module implementation that delegates the initialization to the [platformDelegate]
 */
class CrashHandler(override val platformDelegate: CrashHandlerDelegate) : CrashHandlerInterface {
    override fun initialize() {
        platformDelegate.initialize()
    }
}
