package com.cramsan.framework.userevents.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.userevents.UserEventsInterface

/**
 * This class provides a mechanism to log internal errors and warnings to a [UserEventsInterface].
 */
class UserEventsErrorCallback(private val userEventsInterface: UserEventsInterface) :
    EventLoggerErrorCallbackDelegate {

    override fun handleErrorEvent(
        tag: String,
        message: String,
        throwable: Throwable,
        severity: Severity
    ) {
        userEventsInterface.log(
            tag,
            message,
            mapOf(
                THROWABLE_KEY to (throwable.message ?: throwable.toString()),
                SEVERITY_KEY to severity.name
            )
        )
    }

    companion object {
        private const val THROWABLE_KEY = "Throwable"
        private const val SEVERITY_KEY = "Severity"
    }
}
