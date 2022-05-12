package com.cramsan.stranded.server.repository

class PlayerRepository {

    var eventHandler: EventHandler? = null

    private val playerMapping: MutableMap<String, Player> = mutableMapOf()

    fun createPlayer(playerId: String): Player? {
        if (playerMapping[playerId] != null) {
            return null
        }

        return Player(playerId, playerId, false).let {
            playerMapping[playerId] = it
            it
        }
    }

    fun deletePlayer(playerId: String): Boolean {
        return playerMapping.remove(playerId) != null
    }

    suspend fun updatePlayer(playerId: String, playerName: String): Player? {
        val player = playerMapping[playerId] ?: return null

        return player.copy(name = playerName).let {
            playerMapping[playerId] = it
            eventHandler?.onPlayerUpdated(it)
            it
        }
    }

    fun getPlayer(playerId: String): Player? {
        return playerMapping[playerId]
    }

    suspend fun setPlayerReady(playerId: String, readyToStart: Boolean) {
        val player = playerMapping[playerId] ?: return

        return player.copy(
            readyToStart = readyToStart,
        ).let {
            playerMapping[playerId] = it
            eventHandler?.onPlayerUpdated(it)
        }
    }

    interface EventHandler {
        suspend fun onPlayerUpdated(player: Player)
    }
}
