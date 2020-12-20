package com.cramsan.framework.core

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface

class NoopViewModel @ViewModelInject constructor(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel(application, eventLogger, metricsClient, threadUtil) {
    override val logTag: String
        get() = "NoopViewModel"
}
