package com.cramsan.framework.metrics

/**
 * @Author cramsan
 * @created 1/17/2021
 */
object Metrics {

    lateinit var singleton: MetricsInterface

    fun instance(instance: MetricsInterface? = null): MetricsInterface {
        instance?.let {
            singleton = it
        }
        return singleton
    }
}

/**
 * List of global functions to provide an easy API for logging metrics
 */

fun logMetric(tag: String, event: String) {
    Metrics.singleton.log(tag, event)
}

fun logMetric(tag: String, event: String, metadata: Map<String, String>) {
    Metrics.singleton.log(tag, event, metadata)
}
