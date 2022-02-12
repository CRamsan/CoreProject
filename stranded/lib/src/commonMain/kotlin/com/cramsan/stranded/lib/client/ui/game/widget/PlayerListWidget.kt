package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.server.repository.Player

interface PlayerListWidget : UIComponent {
    fun setPlayerList(playerList: List<GamePlayer>)

    fun updatePlayer(player: Player)
}
