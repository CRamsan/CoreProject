package com.cramsan.stranded.server.repository

import com.cramsan.stranded.server.MultiplayerGameFactory
import com.cramsan.stranded.server.game.MultiplayerGame

/**
 * Repository that will hold and manage the instances of [MultiplayerGame]. To create new instances of [MultiplayerGame],
 * a [MultiplayerGameFactory] is used. Each game will run within a scope that will be managed internally by this instance.
 */
class GameRepository(
    private val multiplayerGameFactory: MultiplayerGameFactory,
) {

    private val gameMapping: MutableMap<String, MultiplayerGame> = mutableMapOf()

    /**
     * Create a [MultiplayerGame] instance and map it to the [lobbyId].
     */
    fun createGame(lobbyId: String): MultiplayerGame? {
        if (gameMapping[lobbyId] != null) {
            return null
        }

        return multiplayerGameFactory.buildMultiplayerGame().let {
            gameMapping[lobbyId] = it
            it
        }
    }

    /**
     * Stop the game mapped to the [lobbyId].
     */
    fun deleteGame(lobbyId: String): Boolean {
        return gameMapping.remove(lobbyId) != null
    }

    /**
     * Retrieve the game mapped to the [lobbyId].
     */
    fun getGame(lobbyId: String): MultiplayerGame? {
        return gameMapping[lobbyId]
    }
}
