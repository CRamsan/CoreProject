package com.cramsan.framework.crashehandler.implementation

import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes

class AppCenterPlatformCrashHandlerInitializer: CrashHandlerPlatformInitializer {
    override fun initialize() {
        AppCenter.start(Crashes::class.java)
    }
}