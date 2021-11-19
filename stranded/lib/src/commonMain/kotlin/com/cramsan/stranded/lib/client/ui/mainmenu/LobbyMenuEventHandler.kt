package com.cramsan.stranded.lib.client.ui.mainmenu

interface LobbyMenuEventHandler {

    fun onLeaveLobbySelected()

    fun onReadySelected(isReady: Boolean)

    fun onStartGameSelected()
}
