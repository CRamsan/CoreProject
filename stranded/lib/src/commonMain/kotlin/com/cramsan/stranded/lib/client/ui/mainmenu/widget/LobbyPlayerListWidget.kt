package com.cramsan.stranded.lib.client.ui.mainmenu.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.server.repository.Player

interface LobbyPlayerListWidget : UIComponent {

    fun updatePlayer(player: Player)

    fun addPlayer(player: Player)

    fun removePlayer(player: Player)

    fun setPlayerList(playerList: List<Player>)
}
