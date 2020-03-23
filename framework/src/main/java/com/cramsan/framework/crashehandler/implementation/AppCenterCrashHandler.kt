package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes

class AppCenterCrashHandler : CrashHandlerInterface {

    override fun initialize() {
        AppCenter.start(Crashes::class.java)
    }
}
