package com.cramsan.stranded.lib.repository

import com.cramsan.stranded.lib.Connection
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.sendEvent

class ConnectionRepository(
    private val lobbyRepository: LobbyRepository
) {

    private val connectionMapping: MutableMap<String, Connection> = mutableMapOf()

    fun registerConnection(connection: Connection) {
        require(!connectionMapping.containsKey(connection.playerId))

        connectionMapping[connection.playerId] = connection
    }

    fun deregisterConnection(connection: Connection) {
        require(connectionMapping.containsKey(connection.playerId))

        connectionMapping.remove(connection.playerId)
    }

    fun getConnection(playerId: String): Connection {
        require(connectionMapping.containsKey(playerId))

        return connectionMapping.getValue(playerId)
    }

    suspend fun broadcastToAll(serverEvent: ServerEvent) {
        connectionMapping.values.forEach {
            it.session.sendEvent(serverEvent)
        }
    }

    suspend fun broadcastToLobby(lobbyId: String, serverEvent: ServerEvent) {
        val lobby = lobbyRepository.getLobby(lobbyId)

        lobby.players.forEach {
            connectionMapping.getValue(it).session.sendEvent(serverEvent)
        }
    }
}
