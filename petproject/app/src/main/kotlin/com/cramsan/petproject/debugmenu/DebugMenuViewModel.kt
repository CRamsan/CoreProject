package com.cramsan.petproject.debugmenu

import android.app.Activity
import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the [DebugMenuFragment].
 */
@HiltViewModel
class DebugMenuViewModel @Inject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    private val modelProvider: ModelProviderInterface,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    override val logTag: String
        get() = "DebugMenuViewModel"

    /**
     * We should remove the use of the [GlobalScope]
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun cleanCache() {
        logI(logTag, "ClearCache")
        GlobalScope.launch(Dispatchers.IO) {
            modelProvider.deleteAll()
        }
    }

    /**
     * Stops the current activity.
     */
    fun killApp(activity: Activity) {
        logI(logTag, "KillApp")
        activity.finishAffinity()
    }
}
