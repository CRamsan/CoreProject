package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface

class MetricsImpl(
    override val platformDelegate: MetricsDelegate,
    private val eventLoggerInterface: EventLoggerInterface,
) : MetricsInterface {

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
