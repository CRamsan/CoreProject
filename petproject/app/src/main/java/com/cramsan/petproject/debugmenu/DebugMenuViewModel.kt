package com.cramsan.petproject.debugmenu

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class DebugMenuViewModel(application: Application) : AndroidViewModel(application), KodeinAware {

    override val kodein by kodein(application)
    private val eventLogger: EventLoggerInterface by instance()
    private val modelProvider: ModelProviderInterface by instance()
    private val TAG = "DebugMenuViewModel"

    fun cleanCache() {
        eventLogger.log(Severity.INFO, TAG, "ClearCache")
        GlobalScope.launch(Dispatchers.IO) {
            modelProvider.deleteAll()
        }
    }

    fun killApp(activity: Activity) {
        eventLogger.log(Severity.INFO, TAG, "KillApp")
        activity.finishAffinity()
    }
}
