package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity

/**
 * Logger that prints to stdout.
 */
class LoggerJVM : EventLoggerDelegate {
    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val pipe = when (severity) {
            Severity.DISABLED -> return
            Severity.VERBOSE, Severity.DEBUG, Severity.INFO -> System.out
            Severity.WARNING, Severity.ERROR -> System.err
        }
        pipe.println("[${severity.name}][$tag]$message")
        throwable?.let {
            it.printStackTrace()
            throwable.printStackTrace()
        }
    }
}
