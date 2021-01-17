package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface

class MetricsImpl(override val platformDelegate: MetricsDelegate) : MetricsInterface {

    override fun initialize() {
        platformDelegate.initialize()
    }

    override fun log(tag: String, event: String) {
        platformDelegate.log(tag, event)
    }

    override fun log(tag: String, event: String, metadata: Map<String, String>) {
        platformDelegate.log(tag, event, metadata)
    }
}
