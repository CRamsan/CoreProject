package com.cramsan.stranded.lib.storage

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import kotlinx.serialization.Serializable

@Serializable
data class DeckHolder(
    val forageCards: List<CardHolder<ScavengeResult>>,
    val nightCards: List<CardHolder<NightEvent>>,
    val belongingsCards: List<CardHolder<Belongings>>,
)
