package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface

actual class AppCenterPlatformCrashHandler(private val crashHandlerPlatformInitializer: AppCenterPlatformCrashHandlerInitializer): CrashHandlerInterface {

    override fun initialize() {
        crashHandlerPlatformInitializer.initialize()
    }
}