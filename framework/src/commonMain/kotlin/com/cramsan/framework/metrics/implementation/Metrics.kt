package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface

class Metrics(initializer: MetricsInitializer): MetricsInterface {

    private val platformMetrics = initializer.platformMetrics

    override fun initialize() {
        platformMetrics.initialize()
    }

    override fun log(event: String) {
        platformMetrics.log(event)
    }
}