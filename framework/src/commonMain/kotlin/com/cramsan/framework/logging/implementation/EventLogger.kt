package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLogger(
    private val targetSeverity: Severity,
    private val errorCallback: EventLoggerErrorCallbackInterface,
    private val platformDelegate: EventLoggerInterface
) : EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String) {
        if (severity < targetSeverity)
            return
        if (severity == Severity.WARNING) {
            errorCallback.onWarning(tag, message)
        } else if (severity == Severity.ERROR) {
            errorCallback.onError(tag, message)
        }
        platformDelegate.log(severity, tag, message)
    }
}
