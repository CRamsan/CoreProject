package com.cramsan.framework.logging

/**
 * Module to log events. Only the events with severity higher or equal to [targetSeverity] will be logged.
 * There is an optional [errorCallback] that can be provided to handle when events with [Severity.WARNING]
 * or [Severity.ERROR]. There is a required [platformDelegate] that implements the logging based on the
 * platform.
 */
interface EventLoggerInterface {

    /**
     * The minimum [Severity] to start logging
     */
    val targetSeverity: Severity

    /**
     * Optional callback to handle when logging events of severity [Severity.WARNING] or [Severity.ERROR]
     */
    val errorCallback: EventLoggerErrorCallback?

    /**
     * Delegate that will implement the logging logic based on the platform
     */
    val platformDelegate: EventLoggerDelegate

    /**
     * Set the [newErrorCallback] as the callback listener for this instance. If there is already a registered
     * instance, it will be replaced with the new [EventLoggerErrorCallback]. [newErrorCallback] can be null.
     */
    fun setErrorCallback(newErrorCallback: EventLoggerErrorCallback?)

    /**
     * Log a [message] and [tag]. If the [severity] is less than [targetSeverity], the message is not logged.
     * There is an optional [throwable] that can be logged.
     *
     * The caller can pass a [ignoreErrorCallback] if they want to skip the [EventLoggerErrorCallback] for calls with
     * severity [Severity.ERROR] or [Severity.WARNING]. This is useful when the caller may be in the path of the
     * [EventLoggerErrorCallback] itself.
     */
    fun log(severity: Severity, tag: String, message: String, throwable: Throwable? = null, ignoreErrorCallback: Boolean = false)

    /**
     * Log a message with [Severity.DEBUG] severity
     */
    fun d(tag: String, message: String) = log(Severity.DEBUG, tag, message)

    /**
     * Log a message with [Severity.VERBOSE] severity
     */
    fun v(tag: String, message: String) = log(Severity.VERBOSE, tag, message)

    /**
     * Log a message with [Severity.INFO] severity
     */
    fun i(tag: String, message: String) = log(Severity.INFO, tag, message)

    /**
     * Log a message with [Severity.WARNING] message
     */
    fun w(tag: String, message: String, throwable: Throwable? = null, ignoreErrorCallback: Boolean = false) = log(Severity.WARNING, tag, message, throwable, ignoreErrorCallback)

    /**
     * Log a message with [Severity.ERROR] message
     */
    fun e(tag: String, message: String, throwable: Throwable? = null, ignoreErrorCallback: Boolean = false) = log(Severity.ERROR, tag, message, throwable, ignoreErrorCallback)
}
