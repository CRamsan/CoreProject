package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricNamespace
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricsInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Implementation of [EventLoggerErrorCallbackDelegate] that reports errors and warning to a [MetricsInterface].
 *
 * This implementation is intended to provide a general insight into the internal status of the running application.
 * It is not a goal to provide a high fidelity report of all errors in the app. We intend to report the number
 * of errors and warnings encountered but also to do so in a way that does not impact the running process.
 *
 * This implementation will perform a best-effort solution and may batch the events before recording them with the
 * [metrics] instance. If the process closes while a batch has not been uploaded, then those metrics will be lost.
 */
class MetricsErrorCallback(
    private val metrics: MetricsInterface,
    private val eventLogger: EventLoggerInterface,
    private val ioDispatcherProvider: DispatcherProvider,
    private val scope: CoroutineScope,
) : EventLoggerErrorCallbackDelegate {

    private var warningCount = 0.0
    private var errorCount = 0.0

    override fun handleErrorEvent(tag: String, message: String, throwable: Throwable, severity: Severity) {
        scope.launch(ioDispatcherProvider.ioDispatcher()) {
            when (severity) {
                Severity.VERBOSE, Severity.DEBUG, Severity.INFO, Severity.DISABLED -> {
                    eventLogger.log(
                        Severity.ERROR,
                        TAG,
                        "MetricsErrorCallback was called for an event of severity $severity",
                        null
                    )
                    dispatchBatch()
                }
                Severity.WARNING -> batchWarningEvent()
                Severity.ERROR -> batchErrorEvent()
            }
        }
    }

    private fun batchWarningEvent() {
        warningCount++
        dispatchBatch()
    }

    private fun batchErrorEvent() {
        errorCount++
        dispatchBatch()
    }

    private fun dispatchBatch() {
        if (warningCount > DISPATCH_THRESHOLD) {
            metrics.record(
                MetricType.EVENT,
                MetricsErrorNamespace,
                Severity.WARNING.name,
                value = warningCount
            )
            warningCount = 0.0
        }
        if (errorCount > DISPATCH_THRESHOLD) {
            metrics.record(
                MetricType.EVENT,
                MetricsErrorNamespace,
                Severity.ERROR.name,
                value = errorCount
            )
            errorCount = 0.0
        }
    }

    /**
     * Namespace for metrics emitted by the [MetricsErrorCallback]. This is supposed to be a small namespace
     * as we are only expected to report very generic and high level metrics. For example, we intend to report
     * overall number of logs with [Severity] of [Severity.ERROR] or [Severity.DEBUG].
     *
     * This name space is not intended to be a fine-grain tool to debug issues.
     */
    object MetricsErrorNamespace : MetricNamespace {
        override val identifier = "Health"
    }

    companion object {
        private const val TAG = "MetricsErrorCallback"
        private const val DISPATCH_THRESHOLD = 3
    }
}
