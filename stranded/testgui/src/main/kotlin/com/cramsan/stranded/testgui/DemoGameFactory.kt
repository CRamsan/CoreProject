package com.cramsan.stranded.testgui

import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.server.MultiplayerGameFactory
import com.cramsan.stranded.server.game.MultiplayerGame
import kotlinx.coroutines.CoroutineScope

/**
 * Factory class that decouples the instantiation of new instances of [MultiplayerGame] from the server.
 *
 * @author cramsan
 */
class DemoGameFactory(val scope: CoroutineScope) : MultiplayerGameFactory {
    override fun buildMultiplayerGame(): Game = Game(scope)
}
