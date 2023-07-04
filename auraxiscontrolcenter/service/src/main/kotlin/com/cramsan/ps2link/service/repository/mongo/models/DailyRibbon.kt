package com.cramsan.ps2link.service.repository.mongo.models

import kotlinx.serialization.Serializable

@Serializable
data class DailyRibbon(
    val count: Int? = null,
    val time: Long? = null,
    val date: String? = null,
)
