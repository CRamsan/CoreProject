package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLoggerImpl(
    override val targetSeverity: Severity,
    override val errorCallback: EventLoggerErrorCallback?,
    override val platformDelegate: EventLoggerDelegate
) : EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        if (severity < targetSeverity)
            return
        platformDelegate.log(severity, tag, message, throwable)
        errorCallback?.let {
            if (severity == Severity.WARNING) {
                it.onWarning(tag, message, throwable)
            } else if (severity == Severity.ERROR) {
                it.onError(tag, message, throwable)
            }
        }
    }
}
