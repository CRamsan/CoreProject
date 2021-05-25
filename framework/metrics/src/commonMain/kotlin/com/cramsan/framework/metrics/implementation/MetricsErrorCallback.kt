package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface

class MetricsErrorCallback(private val metricsInterface: MetricsInterface) :
    EventLoggerErrorCallbackInterface {

    override fun onWarning(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable, Severity.WARNING)
    }

    override fun onError(tag: String, message: String, throwable: Throwable?) {
        logEvent(tag, message, throwable, Severity.ERROR)
    }

    private fun logEvent(tag: String, message: String, throwable: Throwable?, severity: Severity) {
        metricsInterface.log(
            tag,
            message,
            mapOf(
                THROWABLE_KEY to (throwable?.message ?: throwable.toString()),
                SEVERITY_KEY to severity.name
            )
        )
    }

    companion object {
        private const val THROWABLE_KEY = "Throwable"
        private const val SEVERITY_KEY = "Severity"
    }
}
