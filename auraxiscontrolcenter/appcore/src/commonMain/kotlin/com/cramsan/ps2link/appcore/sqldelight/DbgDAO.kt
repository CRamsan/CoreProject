package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.db.Member
import com.cramsan.ps2link.db.Outfit
import kotlinx.coroutines.flow.Flow

interface DbgDAO {

    fun insertCharacter(
        characterId: String,
        name: String?,
        activeProfileId: Long?,
        currentPoints: Long?,
        percentageToNextCert: Double?,
        percentageToNextRank: Double?,
        rank: Long?,
        lastLogin: Long?,
        minutesPlayed: Long?,
        factionId: Faction?,
        worldId: String?,
        outfitName: String?,
        worldName: String?,
        namespace: Namespace,
        lastUpdated: Long,
    )

    fun getAllCharactersAsFlow(): Flow<List<Character>>

    fun removeCharacter(
        characterId: String,
        namespace: Namespace,
    )

    fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character?

    fun insertMember(
        id: String,
        rank: String?,
        outfitId: String?,
        onlineStatus: String?,
        name: String?,
        namespace: Namespace,
        lastUpdated: Long,
    )

    fun insertMemberList(
        memberList: List<Member>,
        lastUpdated: Long,
    )

    fun removeMembersFromOutfit(
        outfitId: String,
        namespace: Namespace,
    )

    fun getCharacters(
        namespace: Namespace,
    ): List<Character>

    fun getAllMembers(
        outfitId: String,
        namespace: Namespace,
    ): List<Member>

    fun getOutfit(
        outfitId: String,
        namespace: Namespace,
    ): Outfit?

    fun getAllOutfits(
        namespace: Namespace,
    ): List<Outfit>

    fun getAllOutfitsAsFlow(): Flow<List<Outfit>>

    fun insertOutfit(
        outfitId: String,
        name: String?,
        alias: String?,
        leaderCharacterId: String?,
        memberCount: Long?,
        timeCreated: Long?,
        worldId: Long?,
        factionId: String?,
        namespace: String,
        lastUpdated: Long,
    )

    fun removeOutfit(
        outfitId: String,
        namespace: Namespace,
    )

    fun deleteAll()
}
