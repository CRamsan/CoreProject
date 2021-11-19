package com.cramsan.stranded.lib.repository

class PlayerRepository(
    private val eventHandler: EventHandler
) {

    private val playerMapping: MutableMap<String, Player> = mutableMapOf()

    fun createPlayer(playerId: String): Player {
        playerMapping[playerId] = Player(
            playerId,
            playerId,
            false
        )

        return playerMapping.getValue(playerId)
    }

    fun deletePlayer(playerId: String) {
        require(playerMapping.containsKey(playerId))

        playerMapping.remove(playerId)
    }

    suspend fun updatePlayer(playerId: String, playerName: String) {
        require(playerMapping.containsKey(playerId))

        val player = playerMapping.getValue(playerId)
        player.name = playerName
        eventHandler.onPlayerUpdated(player)
    }

    fun getPlayer(playerId: String): Player {
        require(playerMapping.containsKey(playerId))

        return playerMapping.getValue(playerId)
    }

    suspend fun setPlayerReady(playerId: String, readyToStart: Boolean) {
        require(playerMapping.containsKey(playerId))

        val player = playerMapping.getValue(playerId)
        player.readyToStart = readyToStart
        eventHandler.onPlayerUpdated(player)
    }

    interface EventHandler {
        suspend fun onPlayerUpdated(player: Player)
    }
}
