package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.ExperienceRank
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponItem
import kotlinx.coroutines.flow.Flow

/**
 * @Author cramsan
 * @created 1/30/2021
 */

interface PS2LinkRepository {

    suspend fun saveCharacter(character: Character)

    suspend fun removeCharacter(characterId: String, namespace: Namespace)

    fun getAllCharactersAsFlow(): Flow<List<Character>>

    suspend fun getAllCharacters(): PS2HttpResponse<List<Character>>

    suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean = false,
    ): PS2HttpResponse<Character?>

    fun getCharacterAsFlow(characterId: String, namespace: Namespace): Flow<Character?>

    suspend fun searchForCharacter(
        searchField: String,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getFriendList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getKillList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<KillEvent>>

    suspend fun getStatList(
        characterId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<StatItem>>

    suspend fun getWeaponList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<WeaponItem>>

    suspend fun getServerList(
        lang: CensusLang,
    ): PS2HttpResponse<List<Server>>

    suspend fun saveOutfit(outfit: Outfit)

    suspend fun removeOutfit(outfitId: String, namespace: Namespace)

    fun getAllOutfitsAsFlow(): Flow<List<Outfit>>

    suspend fun getAllOutfits(): PS2HttpResponse<List<Outfit>>

    suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean = false,
    ): PS2HttpResponse<Outfit>

    fun getOutfitAsFlow(outfitId: String, namespace: Namespace): Flow<Outfit?>

    suspend fun searchForOutfits(
        tagSearchField: String,
        nameSearchField: String,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Outfit>>

    suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    suspend fun getMembers(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>>

    /**
     * Retrieve a single [ExperienceRank] based on the provided arguments.
     * The [filterPrestige] can be used to filter results to only the requested prestige level.
     */
    suspend fun getExperienceRank(
        rank: Int,
        filterPrestige: Int?,
        faction: Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<ExperienceRank?>
}
