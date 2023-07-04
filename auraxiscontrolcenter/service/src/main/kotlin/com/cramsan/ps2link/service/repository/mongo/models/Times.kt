package com.cramsan.ps2link.service.repository.mongo.models

import kotlinx.serialization.Serializable

@Serializable
data class Times(
    val lastLogin: Long? = null,
    val minutesPlayed: Long? = null,
    val creation: Long? = null,
    val loginCount: Long? = null,
)
