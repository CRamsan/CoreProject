package com.cramsan.framework.sample.android.eventlogger

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logV
import com.cramsan.framework.logging.logW
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventLoggerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventLoggerDelegate: EventLoggerDelegate,
) : ViewModel(), EventLoggerScreenEventHandler {

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

    override fun setSeverity(severity: Severity) {
        val instance = EventLoggerImpl(severity, null, eventLoggerDelegate)
        EventLogger.setInstance(instance)
    }

    companion object {
        private const val TAG = "EventLoggerViewModel"
    }
}
