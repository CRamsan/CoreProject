package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.GamePlayer

interface PlayerHeartsWidget : UIComponent {
    val hearts: Int
    fun setContent(player: GamePlayer)
    fun setEnabled(enabled: Boolean)
}
