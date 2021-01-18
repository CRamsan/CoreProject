package com.cramsan.ps2link.appcore

import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.db.Character
import kotlinx.datetime.Clock

/**
 * @Author cramsan
 * @created 1/17/2021
 */

fun CharacterProfile.toCharacter(namespace: Namespace): Character {
    return Character(
        id = character_id,
        name = name?.first,
        activeProfileId = profile_id?.toLong(),
        currentPoints = certs?.available_points?.toLong(),
        percentageToNextCert = certs?.percent_to_next?.toDouble(),
        percentageToNextRank = battle_rank?.percent_to_next?.toDouble(),
        rank = battle_rank?.value?.toLong(),
        lastLogin = times?.last_login?.toLong(),
        minutesPlayed = times?.minutes_played?.toLong(),
        factionId = Faction.fromString(faction_id),
        worldId = world_id,
        outfitName = outfitName,
        worldName = null,
        namespace = namespace.name,
        lastUpdated = Clock.System.now().toEpochMilliseconds(),
    )
}
