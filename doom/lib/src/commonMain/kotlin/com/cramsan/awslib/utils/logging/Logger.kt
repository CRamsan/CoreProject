package com.cramsan.awslib.utils.logging

object Logger {
    private var logger: LoggerInterface = DummyLogger()

    fun log(severity: Severity, message: String) {
        logger.log(severity, message)
    }
}
