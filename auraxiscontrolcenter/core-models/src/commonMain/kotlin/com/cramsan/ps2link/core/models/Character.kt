package com.cramsan.ps2link.core.models

import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * @Author cramsan
 * @created 1/30/2021
 */
@OptIn(ExperimentalTime::class)
data class Character(
    val characterId: String,
    val name: String?,
    val activeProfileId: Long?,
    val certs: Long?,
    val battleRank: Long?,
    val percentageToNextCert: Double?,
    val percentageToNextBattleRank: Double?,
    val lastLogin: Instant?,
    val timePlayed: Duration?,
    val faction: Faction,
    val server: Server?,
    val outfit: Outfit?,
    val namespace: Namespace,
    val cached: Boolean,
)
