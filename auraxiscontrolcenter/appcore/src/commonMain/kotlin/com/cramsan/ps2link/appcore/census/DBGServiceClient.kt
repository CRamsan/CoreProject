package com.cramsan.ps2link.appcore.census

import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.network.models.Namespace
import com.cramsan.ps2link.network.models.content.CharacterEvent
import com.cramsan.ps2link.network.models.content.CharacterFriend
import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.network.models.content.Member
import com.cramsan.ps2link.network.models.content.Outfit
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.WorldEvent
import com.cramsan.ps2link.network.models.content.character.Stats
import com.cramsan.ps2link.network.models.content.item.Weapon
import com.cramsan.ps2link.network.models.content.response.server.PS2

interface DBGServiceClient {
    suspend fun getProfile(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): CharacterProfile?

    /**
     * https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character_name/?name.first_lower=^cram&c:limit=25&c:join=character&c:lang=en
     */
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
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterEvent>?

    /**
     * https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/characters_weapon_stat_by_faction/?character_id=5428010618041058369&c%3Ajoin=item%5Eshow%3Aimage_path%27name.en&c%3Ajoin=vehicle%5Eshow%3Aimage_path%27name.en&c%3Alimit=10000&c%3Alang=en
     */
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
    ): List<CharacterProfile>?

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
