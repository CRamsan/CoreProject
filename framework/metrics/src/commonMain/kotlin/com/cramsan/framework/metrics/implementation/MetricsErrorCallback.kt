package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.metrics.MetricsInterface

class MetricsErrorCallback(private val metricsInterface: MetricsInterface) : EventLoggerErrorCallbackInterface {

    override fun onWarning(tag: String, message: String) {
        metricsInterface.log(tag, message)
    }

    override fun onError(tag: String, message: String) {
        metricsInterface.log(tag, message)
    }
}
