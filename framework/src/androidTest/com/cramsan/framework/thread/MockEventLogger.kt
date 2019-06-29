package com.cramsan.petproject

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class MockEventLogger : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
    }
}