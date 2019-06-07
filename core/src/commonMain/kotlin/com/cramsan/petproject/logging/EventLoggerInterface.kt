package com.cramsan.petproject.logging

interface EventLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
}