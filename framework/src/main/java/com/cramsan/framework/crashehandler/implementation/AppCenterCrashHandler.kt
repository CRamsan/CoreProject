package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes

class AppCenterCrashHandler : CrashHandlerDelegate {

    override fun initialize() {
        AppCenter.start(Crashes::class.java)
    }
}
