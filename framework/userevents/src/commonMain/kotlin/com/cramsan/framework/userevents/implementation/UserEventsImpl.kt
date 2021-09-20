package com.cramsan.framework.userevents.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.userevents.UserEventsDelegate
import com.cramsan.framework.userevents.UserEventsInterface

/**
 * Implementation of [UserEventsInterface] that delegates it's logic to the [platformDelegate] and then
 * it logs the operation to the [eventLoggerInterface].
 *
 * TODO: We should rename this class as it may be confusing between operational events and user events.
 * Maybe we should call it UserEventMetrics.
 */
class UserEventsImpl(
    override val platformDelegate: UserEventsDelegate,
    private val eventLoggerInterface: EventLoggerInterface,
) : UserEventsInterface {

    override fun initialize() {
        platformDelegate.initialize()
    }

    override fun log(tag: String, event: String) {
        logImpl(tag, event, null)
    }

    override fun log(tag: String, event: String, metadata: Map<String, String>) {
        logImpl(tag, event, metadata)
    }

    private fun logImpl(tag: String, event: String, metadata: Map<String, String>?) {
        if (metadata == null) {
            platformDelegate.log(tag, event)
        } else {
            platformDelegate.log(tag, event, metadata)
        }
        eventLoggerInterface.i(TAG, "Logged: $tag - $event. Metadata: $metadata")
    }

    companion object {
        const val TAG = "Metrics"
    }
}
