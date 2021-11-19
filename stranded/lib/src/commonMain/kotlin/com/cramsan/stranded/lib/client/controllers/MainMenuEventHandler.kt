package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.repository.Player

interface MainMenuEventHandler {
    fun onExitSelected()

    fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String)

    fun onDebugGameScreenSelected()
}
