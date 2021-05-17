package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerMetadata
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.response.server.PS2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

class PS2LinkRepositoryImpl(
    private val dbgCensus: DBGServiceClient,
    private val dbgDAO: DbgDAO,
    private val clock: Clock,
) : PS2LinkRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun saveCharacter(character: Character) {
        dbgDAO.insertCharacter(character)
    }

    override suspend fun removeCharacter(characterId: String, namespace: Namespace) {
        dbgDAO.removeCharacter(characterId, namespace)
    }

    override fun getAllCharactersAsFlow(): Flow<List<Character>> {
        return dbgDAO.getAllCharactersAsFlow()
    }

    override suspend fun getAllCharacters(): List<Character> {
        return dbgDAO.getCharacters()
    }

    override suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean,
    ): Character? {
        val cachedCharacter = dbgDAO.getCharacter(characterId, namespace)
        if (!forceUpdate && (cachedCharacter != null && isCharacterValid(cachedCharacter))) {
            return cachedCharacter
        }
        val profile = dbgCensus.getProfile(characterId, namespace, lang) ?: return null
        dbgDAO.insertCharacter(profile.copy(cached = cachedCharacter?.cached ?: false))
        return profile
    }

    override fun getCharacterAsFlow(
        characterId: String,
        namespace: Namespace,
    ): Flow<Character?> {
        return dbgDAO.getCharacterAsFlow(characterId, namespace)
    }

    override suspend fun searchForCharacter(
        searchField: String,
        currentLang: CensusLang,
    ): List<Character> = coroutineScope {
        if (searchField.length < 3) {
            return@coroutineScope emptyList()
        }
        Namespace.validNamespaces.map { namespace ->
            val job = async {
                val endpointProfileList = dbgCensus.getProfiles(
                    searchField = searchField,
                    namespace = namespace,
                    currentLang = currentLang
                )
                endpointProfileList
            }
            job
        }.awaitAll().filterNotNull().flatten()
    }

    override suspend fun getFriendList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<Character> {
        val friendListResponse = dbgCensus.getFriendList(
            character_id = characterId,
            namespace = namespace,
            currentLang = lang,
        )
        return friendListResponse ?: emptyList()
    }

    override suspend fun getKillList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<KillEvent> {
        return dbgCensus.getKillList(
            character_id = characterId,
            namespace = namespace,
            currentLang = lang,
        ) ?: emptyList()
    }

    override suspend fun getStatList(
        characterId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<StatItem> {
        return dbgCensus.getStatList(
            character_id = characterId,
            namespace = namespace,
            currentLang = currentLang,
        ) ?: emptyList()
    }

    override suspend fun getWeaponList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<WeaponItem> {
        val character = getCharacter(characterId, namespace, lang) ?: return emptyList()
        return dbgCensus.getWeaponList(characterId, character.faction, namespace, CensusLang.EN) ?: emptyList()
    }

    override suspend fun getServerList(lang: CensusLang): List<Server> = coroutineScope {
        val serverPopulation = withContext(Dispatchers.Default) { dbgCensus.getServerPopulation() }
        Namespace.validNamespaces.map { namespace ->
            val job = async {
                dbgCensus.getServerList(namespace, lang)?.map {
                    it.world_id?.let { worldId ->
                        val serverMetadata = getServerMetadata(it, serverPopulation, lang)
                        Server(
                            worldId = worldId,
                            serverName = it.name?.localizedName(lang) ?: "",
                            namespace = namespace,
                            serverMetadata = serverMetadata
                        )
                    }
                }
            }
            job
        }.awaitAll().filterNotNull().flatten().filterNotNull()
    }

    override suspend fun saveOutfit(outfit: Outfit) {
        dbgDAO.insertOutfit(outfit)
    }

    override suspend fun removeOutfit(outfitId: String, namespace: Namespace) {
        dbgDAO.removeOutfit(outfitId, namespace)
    }

    override fun getAllOutfitsAsFlow(): Flow<List<Outfit>> {
        return dbgDAO.getAllOutfitsAsFlow()
    }

    override suspend fun getAllOutfits(): List<Outfit> {
        return dbgDAO.getAllOutfits()
    }

    override suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean
    ): Outfit? {
        val cachedOutfit = dbgDAO.getOutfit(outfitId, namespace)
        if (!forceUpdate && (cachedOutfit != null && isOutfitValid(cachedOutfit))) {
            return cachedOutfit
        }
        val outfit = dbgCensus.getOutfit(outfitId, namespace, lang) ?: return null
        dbgDAO.insertOutfit(outfit)
        return outfit
    }

    override fun getOutfitAsFlow(outfitId: String, namespace: Namespace): Flow<Outfit?> {
        return dbgDAO.getOutfitAsFlow(outfitId, namespace)
    }

    override suspend fun searchForOutfits(
        tagSearchField: String,
        nameSearchField: String,
        currentLang: CensusLang,
    ): List<Outfit> = coroutineScope {
        if (tagSearchField.length < 3 && nameSearchField.length < 3) {
            return@coroutineScope emptyList()
        }

        val outfits = Namespace.validNamespaces.map { namespace ->
            val job = async {
                val endpointOutfitList = dbgCensus.getOutfitList(
                    outfitTag = tagSearchField,
                    outfitName = nameSearchField,
                    namespace = namespace,
                    currentLang = currentLang
                )
                endpointOutfitList
            }
            job
        }.awaitAll().filterNotNull().flatten()
        outfits
    }

    override suspend fun getMembersOnline(outfitId: String, namespace: Namespace, currentLang: CensusLang): List<Character> {
        return dbgCensus.getMembersOnline(outfitId, namespace, currentLang) ?: emptyList()
    }

    override suspend fun getMembers(outfitId: String, namespace: Namespace, currentLang: CensusLang): List<Character> {
        return dbgCensus.getMemberList(outfitId, namespace, currentLang) ?: emptyList()
    }

    @OptIn(ExperimentalTime::class)
    private fun isCharacterValid(character: Character?): Boolean {
        character?.lastUpdate.let {
            return if (it == null) {
                false
            } else {
                it + EXPIRATION_TIME < clock.now()
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isOutfitValid(outfit: Outfit?): Boolean {
        outfit?.lastUpdate.let {
            return if (it == null) {
                false
            } else {
                it + EXPIRATION_TIME < clock.now()
            }
        }
    }

    companion object {
        @OptIn(ExperimentalTime::class)
        val EXPIRATION_TIME = 1.minutes
    }
}

private fun getServerMetadata(world: World, ps2Metadata: PS2?, currentLang: CensusLang): ServerMetadata? {
    val server = when (world.name?.localizedName(currentLang)) {
        "Ceres" -> ps2Metadata?.livePS4?.ceres
        "Genudine" -> ps2Metadata?.livePS4?.genudine
        "Cobalt" -> ps2Metadata?.live?.cobalt
        "Connery" -> ps2Metadata?.live?.connery
        "Emerald" -> ps2Metadata?.live?.emerald
        "Miller" -> ps2Metadata?.live?.miller
        else -> null
    }

    val population = server?.status.toCoreModel()
    val status = world.state.toCoreModel()

    return ServerMetadata(
        status = status,
        population = population,
    )
}
