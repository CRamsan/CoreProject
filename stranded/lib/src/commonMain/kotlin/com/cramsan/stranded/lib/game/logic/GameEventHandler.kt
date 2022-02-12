package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange

interface GameEventHandler {

    fun onEventHandled(change: StrandedStateChange)

    fun onPlayerHealthChange(playerId: String, health: Int)

    fun onCardReceived(playerId: String, card: Card)

    fun onCardRemoved(playerId: String, card: Card)
}
