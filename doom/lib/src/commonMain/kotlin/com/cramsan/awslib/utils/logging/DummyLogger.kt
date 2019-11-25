package com.cramsan.awslib.utils.logging

class DummyLogger : LoggerInterface {
    override fun log(severity: Severity, message: String) {
    }
}