package com.cramsan.framework.logging.implementation

import android.util.Log
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class LoggerAndroid : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
        when (severity) {
            Severity.VERBOSE -> Log.v(tag, message)
            Severity.DEBUG -> Log.d(tag, message)
            Severity.INFO -> Log.i(tag, message)
            Severity.WARNING -> Log.w(tag, message)
            Severity.ERROR -> Log.e(tag, message)
        }
    }
}