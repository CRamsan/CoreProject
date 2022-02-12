package com.cramsan.stranded.server.repository

import com.cramsan.stranded.server.Connection

/**
 * Manages the instances of [Connection] that are mapped to each player.
 */
class ConnectionRepository {

    private val connectionMapping: MutableMap<String, Connection> = mutableMapOf()

    /**
     * Register a new [connection].
     */
    fun registerConnection(connection: Connection): Connection? {
        if (connectionMapping.containsKey(connection.playerId)) {
            return null
        }
        connectionMapping[connection.playerId] = connection
        return connection
    }

    /**
     * Deregister an existing [connection].
     */
    fun deregisterConnection(connection: Connection): Boolean {
        return connectionMapping.remove(connection.playerId) != null
    }

    /**
     * Get the [Connection] that is mapped with the [playerId].
     */
    fun getConnection(playerId: String): Connection? {
        return connectionMapping[playerId]
    }
}
