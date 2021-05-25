package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.Severity
import com.microsoft.appcenter.crashes.Crashes

class AppCenterErrorCallback : EventLoggerErrorCallbackInterface {

    override fun onWarning(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable, Severity.WARNING)
    }

    override fun onError(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable, Severity.ERROR)
    }

    private fun logEvent(tag: String, message: String, throwable: Throwable?, severity: Severity) {
        val requiredThrowable = throwable ?: Throwable("")
        Crashes.trackError(
            requiredThrowable,
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
