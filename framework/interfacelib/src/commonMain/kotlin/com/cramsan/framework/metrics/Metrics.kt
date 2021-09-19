package com.cramsan.framework.metrics

import kotlin.native.concurrent.ThreadLocal

/**
 * Singleton that manages an instance of an [MetricsInterface]. The [singleton] starts as
 * null and therefore the caller needs to ensure to provide an instance. If an
 * instance is not set, then any calls to a method that tries to access it will result in a
 * [Throwable] being thrown.
 *
 * @Author cramsan
 * @created 1/17/2021
 *
 */
@ThreadLocal
object Metrics {

    private lateinit var _singleton: MetricsInterface
    val singleton: MetricsInterface
        get() = _singleton

    fun setInstance(assertUtil: MetricsInterface) {
        _singleton = assertUtil
    }
}

/**
 * List of global functions to provide an easy API for logging metrics
 */

/**
 * Global function that delegates to [Metrics.singleton] and calls [MetricsInterface.log].
 *
 * @see MetricsInterface.log
 * @see Metrics.singleton
 */
fun logMetric(tag: String, event: String) {
    Metrics.singleton.log(tag, event)
}

/**
 * Global function that delegates to [Metrics.singleton] and calls [MetricsInterface.log].
 *
 * @see MetricsInterface.log
 * @see Metrics.singleton
 */
fun logMetric(tag: String, event: String, metadata: Map<String, String>) {
    Metrics.singleton.log(tag, event, metadata)
}
