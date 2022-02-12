package com.cramsan.stranded.lib.game.models.night

import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import kotlinx.serialization.Serializable

/**
 * A [NightEvent] is a card that with a [statements] that contains a list of [StrandedStateChange]. This card is drawn at the beginning
 * of the [com.cramsan.stranded.lib.game.logic.Phase.NIGHT] phase.
 */
@Serializable
data class NightEvent(
    override val title: String,
    val statements: List<NightChangeStatement>
) : Card()
