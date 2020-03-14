package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface

class Metrics(initializer: MetricsInitializer): MetricsInterface {

    private val platformMetrics = initializer.platformMetrics

    override fun initialize() {
        platformMetrics.initialize()
    }

    override fun log(tag: String, event: String) {
        platformMetrics.log(tag, event)
    }

    override fun log(tag: String, event: String, metadata: Map<String, String>) {
        platformMetrics.log(tag, event, metadata)
    }
}