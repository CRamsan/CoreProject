package com.cramsan.ps2link.appcore

import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterEvent
import com.cramsan.ps2link.appcore.dbg.content.CharacterFriend
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.appcore.dbg.content.Member
import com.cramsan.ps2link.appcore.dbg.content.Outfit
import com.cramsan.ps2link.appcore.dbg.content.World
import com.cramsan.ps2link.appcore.dbg.content.WorldEvent
import com.cramsan.ps2link.appcore.dbg.content.character.Stats
import com.cramsan.ps2link.appcore.dbg.content.item.Weapon
import com.cramsan.ps2link.appcore.dbg.content.response.server.PS2

interface DBGServiceClient {
    suspend fun getProfile(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): CharacterProfile?

    suspend fun getProfiles(
        searchField: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterProfile>?

    suspend fun getFriendList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterFriend>?

    suspend fun getKillList(
        character_id: String?,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterEvent>?

    suspend fun getWeaponList(
        character_id: String?,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Weapon>?

    suspend fun getOutfitList(
        outfitTag: String,
        outfitName: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Outfit>?

    suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): Outfit?

    suspend fun getMemberList(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Member>?

    suspend fun getServerList(
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<World>?

    suspend fun getServerPopulation(): PS2?

    suspend fun getServerMetadata(
        serverId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<WorldEvent>?

    suspend fun getStatList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): Stats?

    suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Member>?
}
