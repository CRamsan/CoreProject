package com.cramsan.framework.metrics.implementation

import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

class AppCenterPlatformMetricsInitializer : MetricsPlatformInitializer {
    override fun initialize() {
        AppCenter.start(Analytics::class.java)
    }
}