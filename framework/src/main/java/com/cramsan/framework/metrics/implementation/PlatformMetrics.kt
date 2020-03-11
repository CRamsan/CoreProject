package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface
import com.microsoft.appcenter.analytics.Analytics

actual class PlatformMetrics(private val metricsPlatformInitializer: MetricsPlatformInitializer): MetricsInterface {

    override fun initialize() {
        metricsPlatformInitializer.initialize()
    }

    override fun log(event: String) {
        Analytics.trackEvent(event)
    }
}