package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsDelegate
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

/**
 * Implementation of [MetricsDelegate] that logs events to AppCenter.
 */
class AppCenterMetrics : MetricsDelegate {

    override fun initialize() {
        AppCenter.start(Analytics::class.java)
    }

    override fun log(tag: String, event: String) {
        Analytics.trackEvent("$event-$tag")
    }

    override fun log(tag: String, event: String, metadata: Map<String, String>) {
        Analytics.trackEvent("$event-$tag", metadata)
    }
}
