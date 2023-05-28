package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.application.GameSessionManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.Navigator
import org.ocpsoft.prettytime.PrettyTime

/**
 * Initialized instances to be used at the application layer. The classes here pertain to the bejaviour of this
 * application.
 */
class ApplicationLayer(
    frameworkLayer: FrameworkLayer,
    middleLayer: MiddleLayer,
) {

    val hotKeyManager: HotKeyManager

    val gameSessionManager: GameSessionManager

    val navigator: Navigator

    val applicationManager: ApplicationManager

    val prettyTime: PrettyTime

    init {
        hotKeyManager = HotKeyManager(
            json = middleLayer.json,
            preferences = frameworkLayer.preferences,
        )

        gameSessionManager = GameSessionManager(hotKeyManager, middleLayer.applicationScope)

        navigator = Navigator()

        applicationManager = ApplicationManager(
            middleLayer.streamingClient,
            hotKeyManager,
            gameSessionManager,
            frameworkLayer.preferences,
            middleLayer.dbgClient,
            middleLayer.applicationScope,
        )

        prettyTime = PrettyTime()
    }
}
