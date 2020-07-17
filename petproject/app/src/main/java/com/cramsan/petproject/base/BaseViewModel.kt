package com.cramsan.petproject.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), KodeinAware {

    override val kodein by kodein(application)
    protected val eventLogger: EventLoggerInterface by instance()
    protected val metricsClient: MetricsInterface by instance()
    protected val threadUtil: ThreadUtilInterface by instance()

    protected abstract val logTag: String
}
