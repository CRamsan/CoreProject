package com.cramsan.framework.logging

/**
 * Used by [EventLoggerErrorCallback] to handle errors and warnings.
 */
interface EventLoggerErrorCallbackDelegate {
    /**
     * The [tag], [message] and [throwable] were logged with a [Severity] of [Severity.WARNING]
     */
    fun handleErrorEvent(tag: String, message: String, throwable: Throwable, severity: Severity)
}
