package com.cramsan.petproject.base

import android.app.Application
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface

open class NoopViewModel(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
) : BaseViewModel(application, eventLogger, metricsClient, threadUtil) {
    override val logTag: String
        get() = "NoopViewModel"
}
