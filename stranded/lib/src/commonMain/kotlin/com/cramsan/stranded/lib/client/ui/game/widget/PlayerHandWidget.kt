package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card

interface PlayerHandWidget : UIComponent {

    fun setContent(player: GamePlayer)

    fun addCard(card: Card)

    fun removeCard(card: Card)
}
