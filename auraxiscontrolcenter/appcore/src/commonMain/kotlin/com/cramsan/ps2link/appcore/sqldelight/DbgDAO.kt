package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

interface DbgDAO {

    @OptIn(ExperimentalTime::class)
    fun insertCharacter(
        characterId: String,
        name: String?,
        activeProfileId: String?,
        loginStatus: LoginStatus,
        currentPoints: Long?,
        percentageToNextCert: Double?,
        percentageToNextRank: Double?,
        rank: Long?,
        outfitRank: Long?,
        lastLogin: Instant?,
        minutesPlayed: Duration?,
        factionId: Faction,
        worldId: String?,
        worldName: String?,
        outfitId: String?,
        outfitName: String?,
        namespace: Namespace,
        cached: Boolean,
        lastUpdated: Instant,
    )

    fun insertCharacter(character: Character)

    fun getAllCharactersAsFlow(): Flow<List<Character>>

    fun removeCharacter(
        characterId: String,
        namespace: Namespace,
    )

    fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character?

    fun getCharacterAsFlow(
        characterId: String,
        namespace: Namespace,
    ): Flow<Character?>

    fun getCharacters(): List<Character>

    fun getOutfit(
        outfitId: String,
        namespace: Namespace,
    ): Outfit?

    fun getOutfitAsFlow(
        outfitId: String,
        namespace: Namespace,
    ): Flow<Outfit?>

    fun getAllOutfits(): List<Outfit>

    fun getAllOutfitsAsFlow(): Flow<List<Outfit>>

    fun insertOutfit(
        outfitId: String,
        name: String?,
        alias: String?,
        leaderCharacterId: String?,
        leaderCharacterName: String?,
        memberCount: Long?,
        timeCreated: Instant?,
        worldId: String?,
        worldName: String?,
        factionId: Faction,
        namespace: Namespace,
        lastUpdated: Instant,
    )

    fun insertOutfit(outfit: Outfit)

    fun removeOutfit(
        outfitId: String,
        namespace: Namespace,
    )

    fun deleteAll()
}
