package com.cramsan.petproject.debugmenu

import android.app.Activity
import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DebugMenuViewModel @ViewModelInject constructor(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
    dispatcherProvider: DispatcherProvider,
    val modelProvider: ModelProviderInterface,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel(application, eventLogger, metricsClient, threadUtil, dispatcherProvider) {

    override val logTag: String
        get() = "DebugMenuViewModel"

    fun cleanCache() {
        eventLogger.log(Severity.INFO, logTag, "ClearCache")
        GlobalScope.launch(Dispatchers.IO) {
            modelProvider.deleteAll()
        }
    }

    fun killApp(activity: Activity) {
        eventLogger.log(Severity.INFO, logTag, "KillApp")
        activity.finishAffinity()
    }
}
