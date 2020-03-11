package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface

actual class PlatformCrashHandler(private val crashHandlerPlatformInitializer: CrashHandlerPlatformInitializer): CrashHandlerInterface {

    override fun initialize() {
        crashHandlerPlatformInitializer.initialize()
    }
}