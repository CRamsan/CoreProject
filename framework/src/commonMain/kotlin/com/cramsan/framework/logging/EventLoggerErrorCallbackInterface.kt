package com.cramsan.framework.logging

/**
 * Used by the [EventLoggerInterface] to provide a way to handle events logged with [Severity] of
 * [Severity.WARNING] or [Severity.ERROR]
 */
interface EventLoggerErrorCallbackInterface {
    /**
     * The [tag] and [message] were logged with a [Severity] of [Severity.WARNING]
     */
    fun onWarning(tag: String, message: String)

    /**
     * The [tag] and [message] were logged with a [Severity] of [Severity.ERROR]
     */
    fun onError(tag: String, message: String)
}
