package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class LoggerJVM : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
        println(severity.name + tag + message)
    }
}
