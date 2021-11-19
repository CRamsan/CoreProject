package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.common.Card

interface NightCardWidget : UIComponent {

    fun displayCard(card: Card)

    fun hideCard()
}
