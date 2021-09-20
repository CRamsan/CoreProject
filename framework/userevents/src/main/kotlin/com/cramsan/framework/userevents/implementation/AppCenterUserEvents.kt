package com.cramsan.framework.userevents.implementation

import com.cramsan.framework.userevents.UserEventsDelegate
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

/**
 * Implementation of [UserEventsDelegate] that logs events to AppCenter.
 */
class AppCenterUserEvents : UserEventsDelegate {

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
