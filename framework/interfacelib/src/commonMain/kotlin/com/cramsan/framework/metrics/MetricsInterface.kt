package com.cramsan.framework.metrics

/**
 * Module to record operational events.
 */
interface MetricsInterface {

    val platformDelegate: MetricsDelegate

    /**
     * Initialize the module
     */
    fun initialize()

    /**
     * Record a metric event of [type] to be part of the provided [namespace]. The [tag] will be used
     * to uniquely identify this metric. You can pass an optional [metadata] to provide more dimensions
     * to this metric. The [value] and [unit] can be set to whatever fits your use-case.
     */
    fun record(
        type: MetricType,
        namespace: String,
        tag: String,
        metadata: Map<String, String>? = null,
        value: Double = 1.0,
        unit: MetricUnit = MetricUnit.COUNT,
    )
}
