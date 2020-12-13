package com.cramsan.framework.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface

abstract class BaseViewModel(
    application: Application,
    val eventLogger: EventLoggerInterface,
    val metricsClient: MetricsInterface,
    val threadUtil: ThreadUtilInterface
) : AndroidViewModel(application) {

    protected abstract val logTag: String
}
