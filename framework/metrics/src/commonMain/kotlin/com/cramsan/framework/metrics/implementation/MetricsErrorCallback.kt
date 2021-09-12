package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface

/**
 * This class provides a mechanism to log internal errors and warnings to a [MetricsInterface].
 */
class MetricsErrorCallback(private val metricsInterface: MetricsInterface) :
    EventLoggerErrorCallbackDelegate {

    override fun handleErrorEvent(
        tag: String,
        message: String,
        throwable: Throwable,
        severity: Severity
    ) {
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
