package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricNamespace
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface

/**
 * Implementation of [MetricsInterface] that delegates it's logic to the [platformDelegate] and then
 * it logs the operation to the [eventLoggerInterface].
 */
class MetricsImpl(
    override val platformDelegate: MetricsDelegate,
    private val eventLoggerInterface: EventLoggerInterface,
) : MetricsInterface {

    override fun initialize() {
        platformDelegate.initialize()
    }

    override fun record(
        type: MetricType,
        namespace: MetricNamespace,
        tag: String,
        metadata: Map<String, String>?,
        value: Double,
        unit: MetricUnit
    ) {
        eventLoggerInterface.i(TAG, "Metric of type: $type, Namespace: $namespace, Tag: $tag, Metadata: $metadata, Value: $value, Unit: $unit")
        platformDelegate.record(type, namespace.identifier, tag, metadata, value, unit)
    }

    companion object {
        private const val TAG = "Metrics"
    }
}
