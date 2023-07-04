package com.cramsan.ps2link.service.repository.mongo.models

import kotlinx.serialization.Serializable

@Serializable
data class Certs(
    val availablePoints: Int? = null,
    val earnedPoints: Int? = null,
    val giftedPoints: Int? = null,
    val spentPoints: Int? = null,
    val percentToNext: Float? = null,
)
