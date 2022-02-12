package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.server.repository.Player

interface MainMenuEventHandler {
    fun onExitSelected()

    fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String)

    fun onDebugGameScreenSelected()
}
