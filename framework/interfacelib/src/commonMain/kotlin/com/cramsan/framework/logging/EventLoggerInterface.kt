package com.cramsan.framework.logging

/**
 * For convenience, typealias the [EventLoggerInterface] to [Log] to make calling it simpler
 */
typealias Log = EventLoggerInterface

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
    val errorCallback: EventLoggerErrorCallbackInterface?

    /**
     * Delegate that will implement the logging logic based on the platform
     */
    val platformDelegate: EventLoggerDelegate

    /**
     * Log a [message] and [tag]. If the [severity] is less than [targetSeverity], the message is not logged
     */
    fun log(severity: Severity, tag: String, message: String)

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
    fun w(tag: String, message: String) = log(Severity.WARNING, tag, message)

    /**
     * Log a message with [Severity.ERROR] message
     */
    fun e(tag: String, message: String) = log(Severity.ERROR, tag, message)
}
