package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

actual class MockEventLogger : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
    }
}
