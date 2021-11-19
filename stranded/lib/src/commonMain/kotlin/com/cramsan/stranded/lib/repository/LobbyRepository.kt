package com.cramsan.stranded.lib.repository

import com.cramsan.stranded.lib.utils.generateUUID

class LobbyRepository(
    private val playerRepository: PlayerRepository,
    private val eventHandler: EventHandler
) {

    private val lobbyMapping: MutableMap<String, Lobby> = mutableMapOf()
    private val lobbyReverseMapping: MutableMap<String, Lobby> = mutableMapOf()

    suspend fun createLobby(lobbyName: String): Lobby {
        val newLobbyId = generateUUID()
        val newLobby = Lobby(newLobbyId, lobbyName, mutableListOf())
        lobbyMapping[newLobbyId] = newLobby
        eventHandler.onLobbyCreated(newLobby)
        return newLobby
    }

    suspend fun deleteLobby(lobbyId: String) {
        require(lobbyMapping.containsKey(lobbyId))

        lobbyMapping.remove(lobbyId)
        eventHandler.onLobbyDestroyed(lobbyId)
    }

    fun getLobby(lobbyId: String): Lobby {
        require(lobbyMapping.containsKey(lobbyId))

        return lobbyMapping.getValue(lobbyId)
    }

    fun getLobbyForPlayer(playerId: String): Lobby? {
        return lobbyReverseMapping[playerId]
    }

    fun getAllLobbies(): List<Lobby> {
        return lobbyMapping.values.toList()
    }

    suspend fun joinLobby(lobbyId: String, playerId: String) {
        require(lobbyMapping.containsKey(lobbyId))
        require(!lobbyReverseMapping.containsKey(playerId))

        val lobby = lobbyMapping.getValue(lobbyId)
        val player = playerRepository.getPlayer(playerId)

        playerRepository.setPlayerReady(playerId, false)
        lobby.players.add(player.id)
        lobbyReverseMapping[player.id] = lobby
        eventHandler.onPlayerJoined(playerId, lobbyId)
    }

    suspend fun leaveLobby(lobbyId: String, playerId: String) {
        require(lobbyMapping.containsKey(lobbyId))
        require(lobbyReverseMapping.containsKey(playerId))

        val lobby = lobbyMapping.getValue(lobbyId)
        val player = playerRepository.getPlayer(playerId)

        lobbyReverseMapping.remove(player.id)
        eventHandler.onPlayerLeft(playerId, lobbyId)
        lobby.players.remove(player.id)
        playerRepository.setPlayerReady(playerId, false)

        if (lobby.players.isEmpty()) {
            deleteLobby(lobbyId)
        }
    }

    interface EventHandler {
        suspend fun onLobbyCreated(lobby: Lobby)

        suspend fun onLobbyDestroyed(lobbyId: String)

        suspend fun onPlayerJoined(playerId: String, lobbyId: String)

        suspend fun onPlayerLeft(playerId: String, lobbyId: String)
    }
}
