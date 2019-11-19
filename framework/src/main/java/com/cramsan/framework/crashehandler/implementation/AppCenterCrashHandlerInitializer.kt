package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes

class AppCenterCrashHandlerInitializer: CrashHandlerInterface {
    override fun initialize() {
        AppCenter.start(Crashes::class.java)
    }
}