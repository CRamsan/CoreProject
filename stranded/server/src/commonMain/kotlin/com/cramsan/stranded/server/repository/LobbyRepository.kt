package com.cramsan.stranded.server.repository

import com.cramsan.stranded.server.utils.generateUUID

class LobbyRepository(
    private val playerRepository: PlayerRepository,
) {

    var eventHandler: EventHandler? = null

    private val lobbyMapping: MutableMap<String, Lobby> = mutableMapOf()
    private val playerToLobbyMapping: MutableMap<String, String> = mutableMapOf()

    suspend fun createLobby(lobbyName: String): Lobby {
        val newLobbyId = generateUUID()
        val newLobby = Lobby(newLobbyId, lobbyName, listOf(), 8)

        lobbyMapping[newLobbyId] = newLobby
        return newLobby
    }

    suspend fun deleteLobby(lobbyId: String): Boolean {
        return lobbyMapping.remove(lobbyId) != null
    }

    fun getLobby(lobbyId: String): Lobby? {
        return lobbyMapping[lobbyId]
    }

    fun getLobbyForPlayer(playerId: String): String? {
        return playerToLobbyMapping[playerId]
    }

    suspend fun joinLobby(lobbyId: String, playerId: String): Boolean {
        val player = playerRepository.getPlayer(playerId) ?: return false

        playerRepository.setPlayerReady(playerId, false)
        if (!addPlayerToLobby(lobbyId, playerId)) {
            return false
        }

        playerToLobbyMapping[player.id] = lobbyId
        eventHandler?.onPlayerJoined(playerId, lobbyId)
        return true
    }

    suspend fun leaveLobby(lobbyId: String, playerId: String): Boolean {
        val lobby = getLobby(lobbyId) ?: return false
        val player = playerRepository.getPlayer(playerId) ?: return false

        playerRepository.setPlayerReady(playerId, false)
        playerToLobbyMapping.remove(player.id)
        eventHandler?.onPlayerLeft(playerId, lobbyId)
        removePlayerFromLobby(lobbyId, playerId)

        if (lobby.players.isEmpty()) {
            deleteLobby(lobbyId)
        }
        return true
    }

    private fun addPlayerToLobby(lobbyId: String, playerId: String): Boolean {
        playerRepository.getPlayer(playerId) ?: return false
        val lobby = getLobby(lobbyId) ?: return false

        if (lobby.players.size >= lobby.maxPlayers) {
            return false
        }

        lobbyMapping[lobbyId] = lobby.copy(
            players = lobby.players + playerId
        )

        return true
    }

    private fun removePlayerFromLobby(lobbyId: String, playerId: String): Boolean {
        playerRepository.getPlayer(playerId) ?: return false
        val lobby = getLobby(lobbyId) ?: return false

        lobbyMapping[lobbyId] = lobby.copy(
            players = lobby.players.filter { it != playerId }
        )

        return true
    }

    interface EventHandler {
        suspend fun onPlayerJoined(playerId: String, lobbyId: String)

        suspend fun onPlayerLeft(playerId: String, lobbyId: String)
    }
}
