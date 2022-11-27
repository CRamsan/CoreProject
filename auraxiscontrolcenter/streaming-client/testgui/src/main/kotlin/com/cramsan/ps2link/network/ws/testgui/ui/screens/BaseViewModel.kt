package com.cramsan.ps2link.network.ws.testgui.ui.screens

import androidx.compose.runtime.Stable
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.ApplicationUIModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Base implementation for ViewModels. It provides some functions for managing it's lifecycle.
 */
@Stable
abstract class BaseViewModel(
    protected val applicationManager: ApplicationManager,
    protected val dispatcherProvider: DispatcherProvider,
) {

    protected var scope: CoroutineScope? = null

    /**
     * Called when the screen becomes visible.
     */
    open fun onStart() {
        val newScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

        newScope.launch {
            applicationManager.uiModel.collect {
                onApplicationUIModelUpdated(it)
            }
        }

        scope = newScope
    }

    protected abstract fun onApplicationUIModelUpdated(applicationUIModel: ApplicationUIModel)

    /**
     * Called when the screen is hidden/closed.
     */
    abstract fun onClose()
}
