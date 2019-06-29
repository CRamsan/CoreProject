package com.cramsan.framework.logging

class MockEventLogger : EventLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
    }
}