package com.cramsan.ps2link.network.ws.testgui.ui.screens

import androidx.compose.runtime.Stable
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager

/**
 * Base implementation for ViewModels. It provides some functions for managing it's lifecycle.
 */
@Stable
abstract class BaseViewModel(
    protected val applicationManager: ApplicationManager,
) {

    /**
     * Called when the screen becomes visible.
     */
    abstract fun onStart()

    /**
     * Called when the screen is hidden/closed.
     */
    abstract fun onClose()
}
