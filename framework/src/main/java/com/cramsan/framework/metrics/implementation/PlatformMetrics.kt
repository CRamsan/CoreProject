package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface
import com.microsoft.appcenter.analytics.Analytics

actual class PlatformMetrics: MetricsInterface {
    private val metrics = AppCenterMetricsInitializer()

    override fun initialize() {
        metrics.initialize()
    }

    override fun log(event: String) {
        Analytics.trackEvent(event)
    }
}