package com.cramsan.framework.logging

interface PlatformLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
}