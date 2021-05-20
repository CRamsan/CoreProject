package com.cramsan.ps2link.core.models

import kotlinx.datetime.Instant

/**
 * @Author cramsan
 * @created 1/30/2021
 */
data class Outfit(
    val id: String,
    val name: String? = null,
    val tag: String? = null,
    val faction: Faction = Faction.UNKNOWN,
    val server: Server? = null,
    val timeCreated: Instant? = null,
    val leader: Character? = null,
    val memberCount: Int = 0,
    val namespace: Namespace,
    val lastUpdate: Instant? = null,
    val cached: Boolean,
)
