package com.cramsan.ps2link.appcore.census

import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.Vehicle
import com.cramsan.ps2link.core.models.Weapon
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.WorldEvent
import com.cramsan.ps2link.network.models.content.response.server.PS2

interface DBGServiceClient {
    suspend fun getProfile(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<Character?>

    /**
     * https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character_name/?name.first_lower=^cram&c:limit=25&c:join=character&c:lang=en
     */
    suspend fun getProfiles(
        searchField: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getFriendList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getKillList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<KillEvent>>

    /**
     * https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/characters_weapon_stat_by_faction/?character_id=5428010618041058369&c%3Ajoin=item%5Eshow%3Aimage_path%27name.en&c%3Ajoin=vehicle%5Eshow%3Aimage_path%27name.en&c%3Alimit=10000&c%3Alang=en
     */
    suspend fun getWeaponList(
        character_id: String?,
        faction: Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<WeaponItem>>

    suspend fun getOutfitList(
        outfitTag: String,
        outfitName: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Outfit>>

    suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<Outfit>

    suspend fun getMemberList(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getServerList(
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<World>>

    suspend fun getServerPopulation(): PS2HttpResponse<PS2>

    suspend fun getServerMetadata(
        serverId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<WorldEvent>>

    suspend fun getStatList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<StatItem>>

    suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getWeapons(
        weaponIds: List<String>,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Weapon>>

    suspend fun getVehicles(
        vehicleIds: List<String>,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Vehicle>>
}
