package com.cramsan.framework.logging.implementation

import android.util.Log
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class MockEventLogger : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
        when (severity) {
            Severity.VERBOSE -> Log.v(tag, message)
            Severity.DEBUG -> Log.d(tag, message)
            Severity.INFO -> Log.i(tag, message)
            Severity.WARNING -> Log.w(tag, message)
            Severity.ERROR -> Log.e(tag, message)
        }
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (condition)
            return
        log(Severity.ERROR, tag, message)
    }
}