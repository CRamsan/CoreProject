package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.Severity
import com.microsoft.appcenter.crashes.Crashes

/**
 * This class provides a mechanism to log internal errors and warnings to AppCenter.
 */
class AppCenterErrorCallback : EventLoggerErrorCallbackDelegate {

    override fun handleErrorEvent(tag: String, message: String, throwable: Throwable, severity: Severity) {
        Crashes.trackError(
            throwable,
            mapOf(
                TAG_KEY to tag,
                SEVERITY_KEY to severity.name,
                MESSAGE_KEY to message,
            ),
            null,
        )
    }

    companion object {
        private const val TAG_KEY = "Tag"
        private const val MESSAGE_KEY = "Message"
        private const val SEVERITY_KEY = "Severity"
    }
}
