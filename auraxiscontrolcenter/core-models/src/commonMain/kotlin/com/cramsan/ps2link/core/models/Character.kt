package com.cramsan.ps2link.core.models

import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * @Author cramsan
 * @created 1/30/2021
 */
@OptIn(ExperimentalTime::class)
@Suppress("UndocumentedPublicProperty")
data class Character(
    val characterId: String,
    val name: String? = null,
    val activeProfileId: CharacterClass = CharacterClass.UNKNOWN,
    val loginStatus: LoginStatus = LoginStatus.UNKNOWN,
    val certs: Long? = null,
    val battleRank: Long? = null,
    val prestige: Long?,
    val percentageToNextCert: Double? = null,
    val percentageToNextBattleRank: Double? = null,
    val creationTime: Instant?,
    val sessionCount: Long?,
    val lastLogin: Instant? = null,
    val timePlayed: Duration? = null,
    val faction: Faction = Faction.UNKNOWN,
    val server: Server? = null,
    val outfit: Outfit? = null,
    val outfitRank: Rank? = null,
    val namespace: Namespace,
    val lastUpdate: Instant? = null,
    val cached: Boolean,
)
