package com.cramsan.stranded.server.game

import com.cramsan.stranded.server.MultiplayerGameEventHandler

/**
 * Represents an instance of a game implemented for multiple players.
 *
 * @author cramsan
 */
interface MultiplayerGame {

    /**
     * Current state of the game.
     */
    val gameState: GameState

    /**
     * Called when the server has been instructed the game is ready to start by the game host.
     */
    fun onGameStarted()

    /**
     * Called when the server is about to close the game. You should close all your resources in this function.
     */
    fun onGameEnded()

    /**
     * The server received the [playerIntent] for [playerId].
     */
    fun onPlayerIntentReceived(playerId: String, playerIntent: PlayerIntent)

    /**
     * Register the [eventHandler] that will be used to pass instance of [StateChange] back to the server.
     */
    fun registerServerEventHandler(eventHandler: MultiplayerGameEventHandler)

    /**
     * Clear the [MultiplayerGameEventHandler] listener.
     */
    fun deregisterServerEventHandler()
}
