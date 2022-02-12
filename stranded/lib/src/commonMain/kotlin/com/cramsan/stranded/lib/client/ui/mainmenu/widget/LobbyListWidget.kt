package com.cramsan.stranded.lib.client.ui.mainmenu.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.server.repository.Lobby

interface LobbyListWidget : UIComponent {
    fun setLobbyList(lobbyList: List<Lobby>)
}
