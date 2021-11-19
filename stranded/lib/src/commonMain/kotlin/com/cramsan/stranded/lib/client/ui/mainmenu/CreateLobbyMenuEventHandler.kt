package com.cramsan.stranded.lib.client.ui.mainmenu

interface CreateLobbyMenuEventHandler {

    fun onReturnToMainMenuSelected()

    fun onCreateLobbySelected(lobbyName: String)
}
