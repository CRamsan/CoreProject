package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface

class CrashHandler(initializer: CrashHandlerInitializer): CrashHandlerInterface {

    private val platformCrashHandler = initializer.platformCrashHandler

    override fun initialize() {
        platformCrashHandler.initialize()
    }
}