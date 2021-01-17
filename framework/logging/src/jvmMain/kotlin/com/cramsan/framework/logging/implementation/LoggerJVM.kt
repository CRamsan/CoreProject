package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity

class LoggerJVM : EventLoggerDelegate {
    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        println("[${severity.name}][$tag]$message")
        throwable?.let {
            println(it.message)
            throwable.printStackTrace()
        }
    }
}
