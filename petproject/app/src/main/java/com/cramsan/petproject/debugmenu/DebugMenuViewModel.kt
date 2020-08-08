package com.cramsan.petproject.debugmenu

import android.app.Activity
import android.app.Application
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.instance

class DebugMenuViewModel(application: Application) : BaseViewModel(application) {

    private val modelProvider: ModelProviderInterface by instance()
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
