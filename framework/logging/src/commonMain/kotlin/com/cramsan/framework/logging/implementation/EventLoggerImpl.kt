package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

/**
 * Implement the [log] function that is used across all platforms. This class performs the severity
 * check and then it delegates the logging implementation to [platformDelegate]. An [errorCallback]
 * can be provided.
 */
class EventLoggerImpl(
    override val targetSeverity: Severity,
    errorCallback: EventLoggerErrorCallback?,
    override val platformDelegate: EventLoggerDelegate,
) : EventLoggerInterface {

    private var _errorCallback = errorCallback
    override val errorCallback: EventLoggerErrorCallback?
        get() = _errorCallback

    init {
        platformDelegate.setTargetSeverity(targetSeverity)
        Severity.values().forEach {
            log(it, "EventLoggerImpl", "Probing logger for severity: $it", null, false)
        }
    }

    override fun setErrorCallback(newErrorCallback: EventLoggerErrorCallback?) {
        _errorCallback = newErrorCallback
    }

    override fun log(
        severity: Severity,
        tag: String,
        message: String,
        throwable: Throwable?,
        ignoreErrorCallback: Boolean,
    ) {
        if (severity < targetSeverity)
            return

        platformDelegate.log(severity, tag, message, throwable)

        if (ignoreErrorCallback) {
            return
        }
        errorCallback?.let {
            if (severity == Severity.WARNING) {
                it.onWarning(tag, message, throwable)
            } else if (severity == Severity.ERROR) {
                it.onError(tag, message, throwable)
            }
        }
    }
}
