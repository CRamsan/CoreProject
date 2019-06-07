package com.cramsan.petproject.logging

interface PlatformLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
}