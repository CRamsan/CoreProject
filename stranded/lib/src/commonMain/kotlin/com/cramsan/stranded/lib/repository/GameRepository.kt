package com.cramsan.stranded.lib.repository

import com.cramsan.stranded.lib.game.intent.PlayerIntent
import com.cramsan.stranded.lib.game.logic.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel

class GameRepository(
    private val workDispatcher: CoroutineDispatcher
) {

    private val gameMapping: MutableMap<String, Game> = mutableMapOf()
    private val scopeMapping: MutableMap<String, GameScope> = mutableMapOf()

    private var playerQueues: MutableMap<String, Channel<PlayerIntent>> = mutableMapOf()
    private var playerReadySet: MutableMap<String, MutableSet<String>> = mutableMapOf()

    fun createGame(lobbyId: String): Game {
        // TODO: Reenable this check
        // if (players.size < 2) return Result.failure(RuntimeException("Cannot create with less than 2 players"))
        require(!gameMapping.containsKey(lobbyId))
        require(!scopeMapping.containsKey(lobbyId))

        scopeMapping[lobbyId] = GameScope(workDispatcher)
        val newGame = Game(scopeMapping.getValue(lobbyId).scope)
        gameMapping[lobbyId] = newGame

        return newGame
    }

    fun configureGame(gameId: String) {
        require(gameMapping.containsKey(gameId))
        require(scopeMapping.containsKey(gameId))

        val game = gameMapping.getValue(gameId)

        game.playerIntents.forEach {
            playerQueues[it.key] = it.value
        }
        playerReadySet[gameId] = mutableSetOf()
    }

    suspend fun deleteGame(gameId: String) {
        require(gameMapping.containsKey(gameId))
        require(scopeMapping.containsKey(gameId))

        val scope = scopeMapping.getValue(gameId)
        scope.endScope()

        scopeMapping.remove(gameId)
        gameMapping.remove(gameId)
    }

    fun getGame(gameId: String): Game {
        require(gameMapping.containsKey(gameId))
        require(scopeMapping.containsKey(gameId))

        return gameMapping.getValue(gameId)
    }

    fun setPlayerReady(playerId: String, gameId: String): Boolean {
        require(gameMapping.containsKey(gameId))
        require(scopeMapping.containsKey(gameId))

        val set = playerReadySet.getValue(gameId)
        set.add(playerId)

        val result = gameMapping.getValue(gameId)
        if (result.gameState.gamePlayers.size == set.size) {
            return true
        }

        return false
    }
}
