package com.cramsan.awslib.utils.logging

interface LoggerInterface {
    fun log(severity: Severity, message: String)
}