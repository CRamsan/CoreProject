package com.cramsan.ps2link.service.repository.mongo.models

import kotlinx.serialization.Serializable

@Serializable
data class BattleRank(
    val percentToNnext: Float? = null,
    val value: Int? = null,
)
