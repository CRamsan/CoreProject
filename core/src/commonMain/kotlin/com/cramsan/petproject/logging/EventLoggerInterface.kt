package com.cramsan.petproject.logging

interface EventLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
    fun assert(condition: Boolean, tag: String, message: String)
}