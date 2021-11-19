package com.cramsan.stranded.lib.client.ui.mainmenu

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.repository.Player

interface LobbyMenu : UIComponent {

    fun updatePlayer(player: Player)

    fun setPlayerList(playerList: List<Player>)

    fun addPlayer(player: Player)

    fun removePlayer(player: Player)
}
