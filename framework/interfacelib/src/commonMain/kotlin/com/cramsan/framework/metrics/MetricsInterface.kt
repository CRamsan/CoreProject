package com.cramsan.framework.metrics

/**
 * Module to record events around user engagement.
 */
interface MetricsInterface {

    val platformDelegate: MetricsDelegate

    /**
     * Initialize the module
     */
    fun initialize()

    /**
     * Record an instance of the [event] and [tag]
     */
    fun log(tag: String, event: String)

    /**
     * Record an instance of the [event] and [tag] with an included [metadata]
     */
    fun log(tag: String, event: String, metadata: Map<String, String>)
}
