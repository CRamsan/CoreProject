package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.assertlib.assertNotNull
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.onSuccess
import com.cramsan.ps2link.appcore.network.process
import com.cramsan.ps2link.appcore.network.processList
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.network.toFailure
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.ExperienceRank
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerMetadata
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.response.server.PS2
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class PS2LinkRepositoryImpl(
    private val dbgCensus: DBGServiceClient,
    private val dbgDAO: DbgDAO?,
    private val clock: Clock,
) : PS2LinkRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun saveCharacter(character: Character) {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        dbgDAO?.insertCharacter(character)
    }

    override suspend fun removeCharacter(characterId: String, namespace: Namespace) {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        dbgDAO?.removeCharacter(characterId, namespace)
    }

    override fun getAllCharactersAsFlow(): Flow<List<Character>> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return dbgDAO?.getAllCharactersAsFlow() ?: emptyFlow()
    }

    override suspend fun getAllCharacters(): PS2HttpResponse<List<Character>> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return PS2HttpResponse.success(dbgDAO?.getCharacters() ?: emptyList())
    }

    override suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean,
    ): PS2HttpResponse<Character?> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")

        val cachedCharacter = dbgDAO?.getCharacter(characterId, namespace)
        if (!forceUpdate && (cachedCharacter != null && isCharacterValid(cachedCharacter))) {
            return PS2HttpResponse.success(cachedCharacter)
        }
        val response = dbgCensus.getProfile(characterId, namespace, lang)
        response.onSuccess {
            @OptIn(ExperimentalTime::class)
            it?.let {
                dbgDAO?.insertCharacter(it.copy(cached = cachedCharacter?.cached ?: false))
            }
        }
        return response
    }

    override fun getCharacterAsFlow(
        characterId: String,
        namespace: Namespace,
    ): Flow<Character?> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return dbgDAO?.getCharacterAsFlow(characterId, namespace) ?: emptyFlow()
    }

    override suspend fun searchForCharacter(
        searchField: String,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> = coroutineScope {
        if (searchField.length < 3) {
            return@coroutineScope PS2HttpResponse.success(emptyList())
        }
        Namespace.validNamespaces.map { namespace ->
            val job = async {
                val endpointProfileList = dbgCensus.getProfiles(
                    searchField = searchField,
                    namespace = namespace,
                    currentLang = currentLang,
                )
                endpointProfileList
            }
            job
        }.awaitAll().processList { it }.process { it.flatten() }
    }

    override suspend fun getFriendList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        return dbgCensus.getFriendList(
            character_id = characterId,
            namespace = namespace,
            currentLang = lang,
        )
    }

    override suspend fun getKillList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<KillEvent>> {
        return dbgCensus.getKillList(
            character_id = characterId,
            namespace = namespace,
            currentLang = lang,
        )
    }

    override suspend fun getStatList(
        characterId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<StatItem>> {
        return dbgCensus.getStatList(
            character_id = characterId,
            namespace = namespace,
            currentLang = currentLang,
        )
    }

    override suspend fun getWeaponList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): PS2HttpResponse<List<WeaponItem>> {
        val response = getCharacter(characterId, namespace, lang)
        if (!response.isSuccessfulAndContainsBody()) {
            return response.toFailure()
        }
        val character = response.requireBody()
        return dbgCensus.getWeaponList(characterId, character?.faction ?: Faction.UNKNOWN, namespace, lang)
    }

    override suspend fun getServerList(lang: CensusLang): PS2HttpResponse<List<Server>> = coroutineScope {
        val serverPopulation = dbgCensus.getServerPopulation()
        if (!serverPopulation.isSuccessful) {
            return@coroutineScope serverPopulation.toFailure()
        }
        Namespace.validNamespaces.map { namespace ->
            val job = async {
                dbgCensus.getServerList(namespace, lang).process { list ->
                    list.mapNotNull { world ->
                        world.world_id?.let {
                            val serverMetadata =
                                getServerMetadata(world, serverPopulation.requireBody(), lang)
                            Server(
                                worldId = it,
                                serverName = world.name?.localizedName(lang) ?: "",
                                namespace = namespace,
                                serverMetadata = serverMetadata,
                            )
                        }
                    }
                }
            }
            job
        }.awaitAll().processList { it }.process { it.flatten() }
    }

    override suspend fun saveOutfit(outfit: Outfit) {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        dbgDAO?.insertOutfit(outfit)
    }

    override suspend fun removeOutfit(outfitId: String, namespace: Namespace) {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        dbgDAO?.removeOutfit(outfitId, namespace)
    }

    override fun getAllOutfitsAsFlow(): Flow<List<Outfit>> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return dbgDAO?.getAllOutfitsAsFlow() ?: emptyFlow()
    }

    override suspend fun getAllOutfits(): PS2HttpResponse<List<Outfit>> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return PS2HttpResponse.success(dbgDAO?.getAllOutfits() ?: emptyList())
    }

    override suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        lang: CensusLang,
        forceUpdate: Boolean,
    ): PS2HttpResponse<Outfit> {
        val cachedOutfit = dbgDAO?.getOutfit(outfitId, namespace)
        if (!forceUpdate && (cachedOutfit != null && isOutfitValid(cachedOutfit))) {
            return PS2HttpResponse.success(cachedOutfit)
        }
        val response = dbgCensus.getOutfit(outfitId, namespace, lang)
        response.onSuccess {
            dbgDAO?.insertOutfit(it.copy(cached = cachedOutfit?.cached ?: false))
        }
        return response
    }

    override fun getOutfitAsFlow(outfitId: String, namespace: Namespace): Flow<Outfit?> {
        assertNotNull(dbgDAO, TAG, "DbgDAO is null")
        return dbgDAO?.getOutfitAsFlow(outfitId, namespace) ?: emptyFlow()
    }

    override suspend fun searchForOutfits(
        tagSearchField: String,
        nameSearchField: String,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Outfit>> = coroutineScope {
        if (tagSearchField.length < 3 && nameSearchField.length < 3) {
            return@coroutineScope PS2HttpResponse.success(emptyList())
        }

        Namespace.validNamespaces.map { namespace ->
            val job = async {
                val endpointOutfitList = dbgCensus.getOutfitList(
                    outfitTag = tagSearchField,
                    outfitName = nameSearchField,
                    namespace = namespace,
                    currentLang = currentLang,
                )
                endpointOutfitList
            }
            job
        }.awaitAll().processList { it }.process { it.flatten() }
    }

    override suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        val response = dbgCensus.getMembersOnline(outfitId, namespace, currentLang)
        if (!response.isSuccessful) {
            return response.toFailure()
        }
        return response.process { members ->
            members.filter { it.loginStatus != LoginStatus.OFFLINE }
        }
    }

    override suspend fun getMembers(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        return dbgCensus.getMemberList(outfitId, namespace, currentLang)
    }

    override suspend fun getExperienceRank(
        rank: Int,
        filterPrestige: Int?,
        faction: Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<ExperienceRank?> {
        val response = dbgCensus.getExperienceRanks(
            listOf(rank),
            filterPrestige,
            faction,
            namespace,
            currentLang,
        )
        if (!response.isSuccessful) {
            return response.toFailure()
        }

        return response.process {
            it.firstOrNull()
        }
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
        val TAG = "PS2LinkRepositoryImpl"
    }
}

private fun getServerMetadata(world: World, ps2Metadata: PS2?, currentLang: CensusLang): ServerMetadata {
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
