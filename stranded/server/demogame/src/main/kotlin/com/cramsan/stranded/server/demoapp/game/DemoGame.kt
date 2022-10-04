package com.cramsan.stranded.server.demoapp.game

import com.cramsan.stranded.server.MultiplayerGameEventHandler
import com.cramsan.stranded.server.game.MultiplayerGame
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.repository.Player

/**
 * Simple implementation of [MultiplayerGame]. The goal of this class is to provide a small test environment
 * to test the capabilities of the multiplayer server and client implementations.
 *
 * @author cramsan
 */
class DemoGame : MultiplayerGame {

    private var _gameState = DemoGameState(0)

    private var eventHandler: MultiplayerGameEventHandler? = null

    override val gameState: DemoGameState
        get() = _gameState

    override fun onConfigureGame(playerList: List<Player>) = Unit

    override fun onGameStarted() = Unit

    override fun onGameEnded() = Unit

    override fun onPlayerIntentReceived(playerId: String, playerIntent: PlayerIntent) {
        when (playerIntent as DemoPlayerIntent) {
            RequestIncrementCounter -> handleStateChange(IncrementCounter)
        }
    }

    private fun handleStateChange(demoGameStateChange: DemoGameStateChange) {
        _gameState = _gameState.transformWithStateChange(demoGameStateChange)
        eventHandler?.onStateChangeExecuted(demoGameStateChange)
    }

    override fun registerServerEventHandler(eventHandler: MultiplayerGameEventHandler) {
        this.eventHandler = eventHandler
    }

    override fun deregisterServerEventHandler() {
        eventHandler = null
    }
}
