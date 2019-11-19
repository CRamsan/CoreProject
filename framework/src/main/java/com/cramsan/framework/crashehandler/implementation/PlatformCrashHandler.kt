package com.cramsan.framework.crashehandler.implementation

actual class PlatformCrashHandler {

    private val crashHandler = AppCenterCrashHandlerInitializer()

    actual fun initialize() {
        crashHandler.initialize()
    }
}