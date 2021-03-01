package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    var stat_history: List<Stat>? = null,
)
