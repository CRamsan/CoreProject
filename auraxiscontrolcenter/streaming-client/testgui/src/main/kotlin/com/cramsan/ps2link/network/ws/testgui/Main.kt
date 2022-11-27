package com.cramsan.ps2link.network.ws.testgui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import com.cramsan.ps2link.network.ws.testgui.di.ApplicationLayer
import com.cramsan.ps2link.network.ws.testgui.di.FrameworkLayer
import com.cramsan.ps2link.network.ws.testgui.di.MiddleLayer
import com.cramsan.ps2link.network.ws.testgui.ui.ApplicationGUI
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType
import com.cramsan.ps2link.network.ws.testgui.ui.screens.connection.MainScreen
import com.cramsan.ps2link.network.ws.testgui.ui.screens.settings.SettingsScreen

/**
 * Application entry point.
 */
fun main() {

    /**
     * Initialize the core framework dependencies
     */
    val frameworkLayer = FrameworkLayer()

    /**
     * Initialize the PS2Link dependencies
     */
    val middleLayer = MiddleLayer(frameworkLayer)

    /**
     * Initialize the application dependencies
     */
    val applicationLayer = ApplicationLayer(frameworkLayer, middleLayer)

    applicationLayer.hotKeyManager.configureHotKeyManger()

    applicationLayer.applicationManager.startApplication()

    /**
     * Initialize the GUI
     */
    initializeGUI(applicationLayer, middleLayer, frameworkLayer)
}

/**
 * Initialize the GUI component of the application.
 */
fun initializeGUI(applicationLayer: ApplicationLayer, middleLayer: MiddleLayer, frameworkLayer: FrameworkLayer) {
    val applicationManager = applicationLayer.applicationManager
    val hotKeyManager = applicationLayer.hotKeyManager
    val dbgClient = middleLayer.dbgClient
    val dispatcherProvider = frameworkLayer.dispatcherProvider
    val navigator = applicationLayer.navigator
    val prettyTime = applicationLayer.prettyTime

    val settingsScreen = SettingsScreen(
        applicationManager,
        hotKeyManager,
        dispatcherProvider,
    )
    val mainScreen = MainScreen(
        applicationManager,
        dbgClient,
        dispatcherProvider,
        prettyTime,
    )
    navigator.registerScreen(ScreenType.SETTINGS, settingsScreen)
    navigator.registerScreen(ScreenType.MAIN, mainScreen)

    applicationManager.setCurrentScreen(ScreenType.MAIN)

    application {
        val uiModel by applicationManager.uiModel.collectAsState()

        ApplicationGUI(
            navigator,
            applicationManager,
            uiModel,
        )
    }
}
