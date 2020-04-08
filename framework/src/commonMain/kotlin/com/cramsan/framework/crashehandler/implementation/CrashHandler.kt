package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface

class CrashHandler(private val platformDelegate: CrashHandlerInterface) : CrashHandlerInterface {
    override fun initialize() {
        platformDelegate.initialize()
    }
}
