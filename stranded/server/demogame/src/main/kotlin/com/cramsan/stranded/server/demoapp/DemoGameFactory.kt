package com.cramsan.stranded.server.demoapp

import com.cramsan.stranded.server.MultiplayerGameFactory
import com.cramsan.stranded.server.demoapp.game.DemoGame

/**
 * Factory class that decouples the instantiation of new instances of [MultiplayerGame] from the server.
 *
 * @author cramsan
 */
class DemoGameFactory : MultiplayerGameFactory {
    override fun buildMultiplayerGame(): DemoGame = DemoGame()
}
