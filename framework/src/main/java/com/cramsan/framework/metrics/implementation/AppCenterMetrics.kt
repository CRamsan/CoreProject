package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

class AppCenterMetrics: MetricsInterface {

    override fun initialize() {
        AppCenter.start(Analytics::class.java)
    }

    override fun log(tag: String, event: String) {
        Analytics.trackEvent(event, mapOf(KEY_TAG to tag))
    }

    override fun log(tag: String, event: String, metadata: Map<String, String>) {
        val map: MutableMap<String, String> = mutableMapOf(KEY_TAG to tag)
        map.putAll(metadata)
        Analytics.trackEvent(event, map)
    }

    companion object {
        const val KEY_TAG = "Tag"
    }
}