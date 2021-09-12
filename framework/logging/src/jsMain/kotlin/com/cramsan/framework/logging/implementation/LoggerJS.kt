package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity

/**
 * Logger that uses the Javascript APIs.
 */
class LoggerJS : EventLoggerDelegate {
    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val formattedString = "[${severity.name}][$tag]$message"
        when (severity) {
            Severity.VERBOSE, Severity.DEBUG -> console.log(formattedString)
            Severity.INFO -> console.info(formattedString)
            Severity.WARNING -> console.warn(formattedString)
            Severity.ERROR -> console.error(formattedString)
            Severity.DISABLED -> Unit
        }
        throwable?.let {
            console.error(it.message)
            it.printStackTrace()
        }
    }
}
