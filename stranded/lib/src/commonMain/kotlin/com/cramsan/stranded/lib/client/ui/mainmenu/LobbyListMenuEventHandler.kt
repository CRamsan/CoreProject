package com.cramsan.stranded.lib.client.ui.mainmenu

interface LobbyListMenuEventHandler {

    fun onReturnToMainMenuSelected()

    fun onLobbySelected(lobbyId: String)
}
