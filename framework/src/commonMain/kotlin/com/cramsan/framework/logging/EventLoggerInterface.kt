package com.cramsan.framework.logging

interface EventLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
}
