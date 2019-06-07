package com.cramsan.petproject.logging.implementation

import com.cramsan.petproject.logging.EventLoggerInterface
import com.cramsan.petproject.logging.Severity

class EventLogger(platformLogger: PlatformLoggerInterface): EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String) {
        when (severity) {
            Severity.VERBOSE -> TODO()
            Severity.DEBUG -> TODO()
            Severity.INFO -> TODO()
            Severity.WARNING -> TODO()
            Severity.ERROR -> TODO()
        }
    }

}