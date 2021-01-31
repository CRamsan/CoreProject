package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.FriendCharacter
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.StatItem
import kotlinx.coroutines.flow.Flow

/**
 * @Author cramsan
 * @created 1/30/2021
 */

interface PS2LinkRepository {

    suspend fun saveCharacter(character: Character)

    suspend fun removeCharacter(characterId: String, namespace: Namespace)

    fun getAllCharactersAsFlow(): Flow<List<Character>>

    suspend fun getAllCharacters(): List<Character>

    suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean = false,
    ): Character?

    fun getCharacterAsFlow(characterId: String, namespace: Namespace): Flow<Character?>

    suspend fun searchForCharacter(
        searchField: String,
        currentLang: CensusLang,
    ): List<Character>

    suspend fun getFriendList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<FriendCharacter>

    suspend fun getKillList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<KillEvent>

    suspend fun getStatList(
        characterId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<StatItem>
}