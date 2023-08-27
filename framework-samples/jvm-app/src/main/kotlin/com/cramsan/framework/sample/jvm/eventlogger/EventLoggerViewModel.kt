package com.cramsan.framework.sample.jvm.eventlogger

import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logV
import com.cramsan.framework.logging.logW
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.sample.jvm.KEY_LOG_TO_FILE

class EventLoggerViewModel(
    private val eventLoggerDelegate: EventLoggerDelegate,
    private val preferences: Preferences,
) : EventLoggerScreenEventHandler {

    override fun tryLogV() {
        logV(TAG, "This is a verbose message")
    }

    override fun tryLogD() {
        logD(TAG, "This is a debug message")
    }

    override fun tryLogI() {
        logI(TAG, "This is an info message")
    }

    override fun tryLogW() {
        logW(TAG, "This is a warning message", Throwable("Dummy throwable"))
    }

    override fun tryLogE() {
        logE(TAG, "This is an error message", Throwable("Dummy error throwable"))
    }

    override fun toggleLogToFile() {
        val logToFile = preferences.loadBoolean(KEY_LOG_TO_FILE) ?: false
        preferences.saveBoolean(KEY_LOG_TO_FILE, !logToFile)
        val newLogToFile = preferences.loadBoolean(KEY_LOG_TO_FILE) ?: false
        logW(TAG, "Log to file = $newLogToFile")
        logW(TAG, "Restart the application to apply changes")
    }

    override fun setSeverity(severity: Severity) {
        val instance = EventLoggerImpl(severity, null, eventLoggerDelegate)
        EventLogger.setInstance(instance)
    }

    companion object {
        private const val TAG = "EventLoggerViewModel"
    }
}
