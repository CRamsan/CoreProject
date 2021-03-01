package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class BattleRank(
    var percent_to_next: Int = 0,
    var value: Int = 0,
)
