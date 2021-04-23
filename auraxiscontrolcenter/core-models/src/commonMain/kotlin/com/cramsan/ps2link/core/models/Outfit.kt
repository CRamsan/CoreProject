package com.cramsan.ps2link.core.models

import kotlinx.datetime.Instant

/**
 * @Author cramsan
 * @created 1/30/2021
 */
data class Outfit(
    val id: String,
    val name: String?,
    val tag: String?,
    val faction: Faction,
    val worldId: Long,
    val timeCreated: Instant?,
    val leaderCharacterId: String?,
    val memberCount: Int,
    val namespace: Namespace,
)
