package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.Severity

/**
 * Handle the events with severity warning or error. It delegates any further action to the [delegate].
 */
class EventLoggerErrorCallbackImpl(
    private val eventLoggerDelegate: EventLoggerDelegate,
    private val delegate: EventLoggerErrorCallbackDelegate,
) : EventLoggerErrorCallback {

    override fun onWarning(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable ?: ExceptionNotProvided, Severity.WARNING)
    }

    override fun onError(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable ?: ExceptionNotProvided, Severity.ERROR)
    }

    private fun logEvent(tag: String, message: String, throwable: Throwable, severity: Severity) {
        when (severity) {
            Severity.ERROR, Severity.WARNING -> {
                delegate.handleErrorEvent(tag, message, throwable, severity)
                eventLoggerDelegate.log(Severity.DEBUG, TAG, "Handled event with severity: $severity - message: $message", null)
            }
            else -> {
                eventLoggerDelegate.log(
                    Severity.ERROR,
                    TAG,
                    "Could not handle event with severity: $severity - message: $message",
                    InvalidSeverityException
                )
            }
        }
    }

    companion object {
        private const val TAG = "EventLoggerErrorCallback"
        private val ExceptionNotProvided = Throwable("Exception not provided")
        private val InvalidSeverityException = Throwable("EventLoggerErrorCallback called with the wrong severity level. Event not handled.")
    }
}
