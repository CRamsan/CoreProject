package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.getThisMonth
import com.cramsan.ps2link.appcore.getThisWeek
import com.cramsan.ps2link.appcore.getToday
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.setThisMonth
import com.cramsan.ps2link.appcore.setThisWeek
import com.cramsan.ps2link.appcore.setToday
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.toDBModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.appcore.toStatItem
import com.cramsan.ps2link.appcore.toWeaponEventType
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.FriendCharacter
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerMetadata
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.core.models.WeaponStatItem
import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.network.models.content.CharacterEvent
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.character.Day
import com.cramsan.ps2link.network.models.content.character.Month
import com.cramsan.ps2link.network.models.content.character.Stat
import com.cramsan.ps2link.network.models.content.character.Week
import com.cramsan.ps2link.network.models.content.item.StatNameType
import com.cramsan.ps2link.network.models.content.item.Weapon
import com.cramsan.ps2link.network.models.content.response.server.PS2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
        if (!forceUpdate && (cachedCharacter != null && isCharacterValid(cachedCharacter.toDBModel(clock.now().toEpochMilliseconds())))) {
            return cachedCharacter
        }
        val profile = dbgCensus.getProfile(characterId, namespace.toNetworkModel(), lang)
            ?.toCoreModel(namespace.toDBModel(), clock.now().toEpochMilliseconds(), lang) ?: return null
        dbgDAO.insertCharacter(profile)
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
                    namespace = namespace.toNetworkModel(),
                    currentLang = currentLang
                )
                endpointProfileList?.map {
                    it.toCoreModel(namespace.toDBModel(), clock.now().toEpochMilliseconds(), currentLang)
                }
            }
            job
        }.awaitAll().filterNotNull().flatten()
    }

    override suspend fun getFriendList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<FriendCharacter> {
        val friendListResponse = dbgCensus.getFriendList(
            character_id = characterId,
            namespace = namespace.toNetworkModel(),
            currentLang = lang,
        )
        return friendListResponse?.mapNotNull { friend ->
            friend.character_id?.let { id ->
                FriendCharacter(
                    id,
                    friend.name?.first,
                    namespace,
                    LoginStatus.fromString(friend.online.toString())
                )
            }
        } ?: emptyList()
    }

    override suspend fun getKillList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<KillEvent> {
        val killListResponse = dbgCensus.getKillList(
            character_id = characterId,
            namespace = namespace.toNetworkModel(),
            currentLang = lang,
        )
        return formatKillList(characterId, killListResponse, namespace)
    }

    override suspend fun getStatList(
        characterId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<StatItem> {
        val statListResponse = dbgCensus.getStatList(
            character_id = characterId,
            namespace = namespace.toNetworkModel(),
            currentLang = currentLang,
        )?.stat_history
        return formatStats(statListResponse)
    }

    override suspend fun getWeaponList(
        characterId: String,
        namespace: Namespace,
        lang: CensusLang,
    ): List<WeaponItem> {
        val weaponListResponse = dbgCensus.getWeaponList(characterId, namespace.toNetworkModel(), CensusLang.EN)
        return formatWeapons(weaponListResponse, lang)
    }

    override suspend fun getServerList(lang: CensusLang): List<Server> = coroutineScope {
        val serverPopulation = withContext(Dispatchers.Default) { dbgCensus.getServerPopulation() }
        Namespace.validNamespaces.map { namespace ->
            val job = async {
                dbgCensus.getServerList(namespace.toNetworkModel(), lang)?.map {
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
        if (!forceUpdate && (cachedOutfit == null || isOutfitValid(cachedOutfit.toDBModel(clock.now().toEpochMilliseconds())))) {
            return cachedOutfit
        }
        val outfit = dbgCensus.getOutfit(outfitId, namespace.toNetworkModel(), lang)
            ?.toCoreModel(namespace.toDBModel(), clock.now().toEpochMilliseconds()) ?: return null
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
                    namespace = namespace.toNetworkModel(),
                    currentLang = currentLang
                )
                endpointOutfitList?.map {
                    it.toCoreModel(namespace.toDBModel(), clock.now().toEpochMilliseconds())
                }
            }
            job
        }.awaitAll().filterNotNull().flatten()
        outfits
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

    private fun formatWeapons(weaponList: List<Weapon>?, currentLang: CensusLang): List<WeaponItem> {
        if (weaponList == null) {
            return emptyList()
        }

        val weaponMap = weaponList.groupBy { it.item_id }
        return weaponMap.map {
            val weaponId = it.key
            val weaponStats = it.value

            var weaponName: String? = null
            var vehicleName: String? = null
            var weaponUrl: String? = null
            val statMapping = mutableMapOf<WeaponEventType, WeaponStatItem>()
            for (stat in weaponStats) {
                val weaponEventType = StatNameType.fromString(stat.stat_name)?.toWeaponEventType() ?: continue
                weaponName = stat.item_id_join_item?.name?.localizedName(currentLang)
                vehicleName = stat.vehicle_id_join_vehicle?.name?.localizedName(CensusLang.EN)
                weaponUrl = if (stat.item_id_join_item != null) {
                    stat.item_id_join_item?.image_path
                } else if (stat.vehicle_id_join_vehicle != null) {
                    stat.vehicle_id_join_vehicle?.image_path
                } else {
                    null
                }
                statMapping[weaponEventType] = WeaponStatItem(
                    mapOf(
                        com.cramsan.ps2link.core.models.Faction.TR to stat.value_tr.toLong(),
                        com.cramsan.ps2link.core.models.Faction.NC to stat.value_nc.toLong(),
                        com.cramsan.ps2link.core.models.Faction.VS to stat.value_vs.toLong(),
                    )
                )
            }

            WeaponItem(
                weaponId = weaponId,
                weaponName = weaponName,
                vehicleName = vehicleName,
                weaponImage = weaponUrl,
                statMapping = statMapping,
            )
        }
    }

    private fun formatStats(stats: List<Stat>?): List<StatItem> {
        if (stats == null) {
            return emptyList()
        }

        var kills: Stat? = null
        var deaths: Stat? = null
        var score: Stat? = null
        var time: Stat? = null
        for (stat in stats) {
            if (stat.stat_name == "kills") {
                kills = stat
            } else if (stat.stat_name == "deaths") {
                deaths = stat
            } else if (stat.stat_name == "score") {
                score = stat
            } else if (stat.stat_name == "time") {
                time = stat
            }
        }
        val kdr = Stat()
        deaths?.let {
            if (deaths.all_time == "0") {
                deaths.all_time = "1"
            }
            if (deaths.getToday() == 0f) {
                deaths.setToday(1F)
            }
            if (deaths.getThisWeek() == 0f) {
                deaths.setThisWeek(1F)
            }
            if (deaths.getThisMonth() == 0f) {
                deaths.setThisMonth(1F)
            }
        }
        kdr.day = Day()
        kdr.week = Week()
        kdr.month = Month()

        kdr.stat_name = "kdr"
        kdr.all_time = (
            (kills?.all_time?.toFloatOrNull() ?: 0f) / (deaths?.all_time?.toFloatOrNull() ?: 1f)
            ).toString()

        kdr.setToday((kills?.getToday() ?: 0f) / (deaths?.getToday() ?: 1f))
        kdr.setThisWeek((kills?.getThisWeek() ?: 0f) / (deaths?.getThisWeek() ?: 1f))
        kdr.setThisMonth((kills?.getThisMonth() ?: 0f) / (deaths?.getThisMonth() ?: 1f))

        val results = mutableListOf<Stat>()
        results.add(kdr)

        val sph = Stat()
        time?.let {
            if (time.all_time == "0") {
                time.all_time = "1"
            }
            if (time.getToday() == 0f) {
                time.setToday(1F)
            }
            if (time.getThisWeek() == 0f) {
                time.setThisWeek(1F)
            }
            if (time.getThisMonth() == 0f) {
                time.setThisMonth(1F)
            }
        }
        sph.day = Day()
        sph.week = Week()
        sph.month = Month()

        // TODO: Replace this with a resource
        sph.stat_name = "Score/Hour"
        sph.all_time = (
            (score?.all_time?.toFloatOrNull() ?: 0f) / ((time?.all_time?.toFloatOrNull() ?: 1f) / 3600f)
            ).toString()
        sph.setToday((score?.getToday() ?: 0f) / ((time?.getToday() ?: 3600f) / 3600f))
        sph.setThisWeek((score?.getThisWeek() ?: 0f) / ((time?.getThisWeek() ?: 3600f) / 3600f))
        sph.setThisMonth((score?.getThisMonth() ?: 0f) / ((time?.getThisMonth() ?: 3600f) / 3600f))
        results.add(sph)

        results.addAll(stats)
        return results.map {
            it.toStatItem()
        }
    }

    fun formatKillList(characterId: String, characterEventList: List<CharacterEvent>?, namespace: Namespace): List<KillEvent> {
        if (characterEventList == null) {
            return emptyList()
        }

        return characterEventList.map {
            val weaponName = it.weapon_name
            val attackerName: String?
            val time = it.timestamp?.toLong()
            val killType: KillType
            var eventCharacterId: String? = null
            val faction: Faction = Faction.fromString(it.attacker?.faction_id)

            if (it.attacker_character_id == characterId) {
                attackerName = it.character?.name?.first
                it.important_character_id = it.character_id
                if (it.character_id == characterId) {
                    killType = KillType.SUICIDE
                } else {
                    killType = KillType.KILL
                    eventCharacterId = it.character_id
                }
            } else if (it.character_id == characterId) {
                killType = KillType.KILLEDBY
                attackerName = it.attacker?.name?.first
                eventCharacterId = it.attacker_character_id
                it.important_character_id = it.attacker_character_id
            } else {
                killType = KillType.UNKNOWN
                attackerName = null
            }

            KillEvent(
                characterId = eventCharacterId,
                namespace = namespace,
                killType = killType,
                faction = faction.toCoreModel(),
                attacker = attackerName,
                time = time,
                weaponName = weaponName,
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isCharacterValid(character: com.cramsan.ps2link.db.Character?): Boolean {
        if (character == null) {
            return false
        }

        return Instant.fromEpochMilliseconds(character.lastUpdated) + EXPIRATION_TIME < clock.now()
    }

    @OptIn(ExperimentalTime::class)
    private fun isOutfitValid(outfit: com.cramsan.ps2link.db.Outfit?): Boolean {
        if (outfit == null) {
            return false
        }

        return Instant.fromEpochMilliseconds(outfit.lastUpdated) + EXPIRATION_TIME < clock.now()
    }

    companion object {
        @OptIn(ExperimentalTime::class)
        val EXPIRATION_TIME = 1.minutes
    }
}
