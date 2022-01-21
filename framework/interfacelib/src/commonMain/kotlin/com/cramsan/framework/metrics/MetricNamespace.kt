package com.cramsan.framework.metrics

/**
 * Use this interface to declare new namespaces. This is to be used for the [MetricsInterface].
 */
interface MetricNamespace {

    /**
     * String representation of the [MetricsInterface].
     */
    val identifier: String
}
