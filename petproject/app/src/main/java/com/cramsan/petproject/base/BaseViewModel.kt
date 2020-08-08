package com.cramsan.petproject.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), DIAware {

    override val di by di(application)
    protected val eventLogger: EventLoggerInterface by instance()
    protected val metricsClient: MetricsInterface by instance()
    protected val threadUtil: ThreadUtilInterface by instance()

    protected abstract val logTag: String
}
