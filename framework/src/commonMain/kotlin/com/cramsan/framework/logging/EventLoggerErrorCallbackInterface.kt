package com.cramsan.framework.logging

interface EventLoggerErrorCallbackInterface {
    fun onWarning(tag: String, message: String)

    fun onError(tag: String, message: String)
}
