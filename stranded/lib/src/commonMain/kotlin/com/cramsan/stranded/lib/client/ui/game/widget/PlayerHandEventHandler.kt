package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.game.models.common.Card

interface PlayerHandEventHandler {

    fun onCardSelected(card: Card)
}
