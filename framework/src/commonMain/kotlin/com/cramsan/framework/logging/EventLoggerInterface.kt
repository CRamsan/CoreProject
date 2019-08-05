package com.cramsan.framework.logging

interface EventLoggerInterface {
    fun log(severity: Severity, tag: String, message: String)
    fun assert(condition: Boolean, tag: String, message: String)
}

fun Any.classTag(): String {
    return this.toString()
}
