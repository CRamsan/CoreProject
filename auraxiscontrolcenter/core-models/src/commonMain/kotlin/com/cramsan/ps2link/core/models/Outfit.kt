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
    val worldId: Long? = null,
    val timeCreated: Instant? = null,
    val leaderCharacterId: String? = null,
    val memberCount: Int = 0,
    val namespace: Namespace = Namespace.UNDETERMINED,
)
