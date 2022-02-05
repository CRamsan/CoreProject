package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase

interface PlayerHandWidget : UIComponent {

    fun setHandContent(player: GamePlayer)

    fun addCard(card: Card)

    fun removeCard(card: Card)

    fun setPhaseForHand(gamePhase: Phase)
}
