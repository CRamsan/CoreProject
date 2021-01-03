package com.cramsan.framework.core

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel(
    application: Application,
    val eventLogger: EventLoggerInterface,
    val metricsClient: MetricsInterface,
    val threadUtil: ThreadUtilInterface,
    val dispatcherProvider: DispatcherProvider,
) : AndroidViewModel(application) {

    protected abstract val logTag: String

    protected val ioScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

    protected val events = LiveEvent<BaseEvent>()
    fun events() = events.asLiveData()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        ioScope.cancel()
    }
}
