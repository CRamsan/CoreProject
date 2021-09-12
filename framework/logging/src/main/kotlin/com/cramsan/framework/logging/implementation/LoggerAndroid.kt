package com.cramsan.framework.logging.implementation

import android.util.Log
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity

/**
 * Logger that outputs to the standard Android logger.
 */
class LoggerAndroid : EventLoggerDelegate {
    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        when (severity) {
            Severity.VERBOSE -> Log.v(tag, message)
            Severity.DEBUG -> Log.d(tag, message)
            Severity.INFO -> Log.i(tag, message)
            Severity.WARNING -> Log.w(tag, message, throwable)
            Severity.ERROR -> Log.e(tag, message, throwable)
            Severity.DISABLED -> Unit
        }
    }
}
