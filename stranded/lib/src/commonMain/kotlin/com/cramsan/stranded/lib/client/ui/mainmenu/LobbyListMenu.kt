package com.cramsan.stranded.lib.client.ui.mainmenu

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.server.repository.Lobby

interface LobbyListMenu : UIComponent {

    fun setLobbyList(lobbyList: List<Lobby>)
}
