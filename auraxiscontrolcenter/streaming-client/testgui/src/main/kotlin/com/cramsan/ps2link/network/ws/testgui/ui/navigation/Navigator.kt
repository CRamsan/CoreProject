package com.cramsan.ps2link.network.ws.testgui.ui.navigation

import androidx.compose.runtime.Composable
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseScreen

/**
 * Simple navigation manager. Classes that extend [BaseScreen] can be registered with [registerScreen].
 */
class Navigator {

    private val mappedScreens = mutableMapOf<ScreenType, BaseScreen<*>>()

    /**
     * Register [baseScreen] to be associated with [screenType].
     */
    fun registerScreen(screenType: ScreenType, baseScreen: BaseScreen<*>) {
        if (mappedScreens.contains(screenType)) {
            logE(
                TAG,
                "Screen for type $screenType is already registered. " +
                    "Ignoring screen of type ${baseScreen.javaClass.canonicalName}.",
            )
            return
        }

        if (screenType == ScreenType.UNDEFINED) {
            logE(TAG, "Cannot register screen for type ScreenType.UNDEFINED.")
            return
        }

        logD(TAG, "Screen ${baseScreen.javaClass.canonicalName} registered for type $screenType.")
        mappedScreens[screenType] = baseScreen
    }

    /**
     * Composable function to display one of the registered screens. The [screenType] decides which screen will be
     * displayed.
     */
    @Composable
    fun renderScreen(screenType: ScreenType) {
        mappedScreens.forEach {
            it.value.ComposeScreen(it.key == screenType)
        }
    }

    companion object {
        private const val TAG = "Navigator"
    }
}
