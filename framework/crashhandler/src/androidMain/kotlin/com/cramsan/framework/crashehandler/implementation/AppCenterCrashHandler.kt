package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes

/**
 * AppCenter CrashHandler for Android. Calling [initialize] will ensure that the library is configured
 * and ready to catch crashes. No other action is required from the caller.
 */
class AppCenterCrashHandler : CrashHandlerDelegate {

    override fun initialize() {
        AppCenter.start(Crashes::class.java)
    }
}
