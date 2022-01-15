package com.cramsan.stranded.lib.storage

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import kotlinx.serialization.Serializable

@Serializable
data class DeckHolder(
    val forageCards: List<MutableCardHolder<ScavengeResult>>,
    val nightCards: List<MutableCardHolder<NightEvent>>,
    val belongingsCards: List<MutableCardHolder<Belongings>>,
)
