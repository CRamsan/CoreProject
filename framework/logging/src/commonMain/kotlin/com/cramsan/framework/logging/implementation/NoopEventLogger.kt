package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class NoopEventLogger : EventLoggerInterface {
    override val targetSeverity: Severity
        get() = TODO("Not yet implemented")
    override val errorCallback: EventLoggerErrorCallback?
        get() = TODO("Not yet implemented")
    override val platformDelegate: EventLoggerDelegate
        get() = TODO("Not yet implemented")

    override fun setErrorCallback(newErrorCallback: EventLoggerErrorCallback?) = Unit
    override fun log(
        severity: Severity,
        tag: String,
        message: String,
        throwable: Throwable?,
        ignoreErrorCallback: Boolean
    ) = Unit
}
