package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class BattleRank(
    val percent_to_next: Int? = 0,
    val value: Int? = 0,
)
