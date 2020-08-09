package com.cramsan.framework.logging

/**
 * Delegate to be implemented for each platform to provide logging
 */
interface EventLoggerDelegate {

    /**
     * Log a [message] and [tag]. If the [severity] is less than [targetSeverity], the message is not logged
     */
    fun log(severity: Severity, tag: String, message: String)
}
