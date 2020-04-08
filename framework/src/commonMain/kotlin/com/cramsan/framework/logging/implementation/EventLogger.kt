package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class EventLogger(private val targetSeverity: Severity,
                  private val platformDelegate: EventLoggerInterface
) : EventLoggerInterface {

    override fun log(severity: Severity, tag: String, message: String) {
        if (severity < targetSeverity)
            return
        platformDelegate.log(severity, tag, message)
    }
}
