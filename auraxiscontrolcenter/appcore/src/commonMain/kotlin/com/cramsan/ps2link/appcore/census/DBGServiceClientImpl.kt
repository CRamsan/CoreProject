package com.cramsan.ps2link.appcore.census

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.characterClassFromString
import com.cramsan.ps2link.appcore.getThisMonth
import com.cramsan.ps2link.appcore.getThisWeek
import com.cramsan.ps2link.appcore.getToday
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.network.process
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.network.toFailure
import com.cramsan.ps2link.appcore.setThisMonth
import com.cramsan.ps2link.appcore.setThisWeek
import com.cramsan.ps2link.appcore.setToday
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.appcore.toStatItem
import com.cramsan.ps2link.appcore.toWeaponEventType
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.ExperienceRank
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Rank
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.Vehicle
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.core.models.WeaponStatItem
import com.cramsan.ps2link.core.models.toMedalType
import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.metric.HttpNamespace
import com.cramsan.ps2link.network.models.Verb
import com.cramsan.ps2link.network.models.content.CharacterEvent
import com.cramsan.ps2link.network.models.content.CharacterFriend
import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.network.models.content.Member
import com.cramsan.ps2link.network.models.content.Outfit
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.WorldEvent
import com.cramsan.ps2link.network.models.content.character.Stat
import com.cramsan.ps2link.network.models.content.item.StatNameType
import com.cramsan.ps2link.network.models.content.item.WeaponStat
import com.cramsan.ps2link.network.models.content.response.Character_friend_list_response
import com.cramsan.ps2link.network.models.content.response.Character_list_response
import com.cramsan.ps2link.network.models.content.response.Character_name_list_response
import com.cramsan.ps2link.network.models.content.response.Characters_event_list_response
import com.cramsan.ps2link.network.models.content.response.ExperienceRankResponse
import com.cramsan.ps2link.network.models.content.response.Item_list_response
import com.cramsan.ps2link.network.models.content.response.Outfit_member_response
import com.cramsan.ps2link.network.models.content.response.Outfit_response
import com.cramsan.ps2link.network.models.content.response.Server_Status_response
import com.cramsan.ps2link.network.models.content.response.Server_response
import com.cramsan.ps2link.network.models.content.response.Vehicle_list_response
import com.cramsan.ps2link.network.models.content.response.Weapon_list_response
import com.cramsan.ps2link.network.models.content.response.World_event_list_response
import com.cramsan.ps2link.network.models.content.response.server.PS2
import com.cramsan.ps2link.network.models.util.Collections
import com.cramsan.ps2link.network.models.util.QueryString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

/**
 * This class will be in charge of formatting requests for DBG Census API and
 * retrieving the information. You can use the response directly from JSON or
 * they can be also automatically converted to objects to ease their
 * manipulation.
 *
 *
 * API Calls follow the following format:
 * /verb/game/collection/[identifier]?[queryString]
 *
 *
 * This class is been designed by following the design specified on
 * http://census.daybreakgames.com/.
 */

class DBGServiceClientImpl(
    private val census: DBGCensus,
    private val http: HttpClient,
    private val clock: Clock,
) : DBGServiceClient {

    override suspend fun getProfile(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<Character?> {
        logI(TAG, "Downloading Profile")
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString().AddCommand(
                QueryString.QueryCommand.RESOLVE,
                "outfit,world,online_status",
            )
                .AddCommand(QueryString.QueryCommand.JOIN, "type:world^inject_at:server"),
            HttpNamespace.Api.PROFILE,
            namespace.toNetworkModel(),
            currentLang,
        )
        val response = http.sendRequestWithRetry<Character_list_response>(url)
        return response.process {
            it.character_list?.firstOrNull()?.toCoreModel(namespace, clock.now(), currentLang)
        }
    }

    override suspend fun getProfiles(
        searchField: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        logI(TAG, "Downloading Profile List")
        if (searchField.length < 3) {
            return PS2HttpResponse.success(emptyList())
        }
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER_NAME,
            "",
            QueryString.generateQeuryString()
                .AddComparison(
                    "name.first_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    searchField.lowercase(),
                )
                .AddCommand(QueryString.QueryCommand.LIMIT, "25")
                .AddCommand(QueryString.QueryCommand.JOIN, "character"),
            HttpNamespace.Api.PROFILE_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Character_name_list_response>(url)
        return response.process { characterList ->
            characterList.character_name_list?.map {
                it.character_id_join_character?.toCoreModel(
                    namespace,
                    clock.now(),
                    currentLang,
                )
            }?.filterNotNull() ?: emptyList()
        }
    }

    override suspend fun getFriendList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTERS_FRIEND,
            null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                QueryString.SearchModifier.EQUALS,
                character_id,
            )
                .AddCommand(QueryString.QueryCommand.RESOLVE, "character_name"),
            HttpNamespace.Api.FRIEND_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Character_friend_list_response>(url)
        return response.process { friendList ->
            friendList.characters_friend_list.first().friend_list.map { it.toCoreModel(namespace) }
        }
    }

    override suspend fun getKillList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<KillEvent>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTERS_EVENT,
            null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                QueryString.SearchModifier.EQUALS,
                character_id,
            )
                .AddCommand(
                    QueryString.QueryCommand.RESOLVE,
                    "character,attacker",
                ).AddCommand(QueryString.QueryCommand.LIMIT, "100")
                .AddComparison("type", QueryString.SearchModifier.EQUALS, "DEATH,KILL"),
            HttpNamespace.Api.KILL_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Characters_event_list_response>(url)
        if (!response.isSuccessful) {
            return response.toFailure()
        }
        val weaponIds = response.requireBody().characters_event_list?.mapNotNull { event ->
            event.attacker_weapon_id
        }?.distinct() ?: emptyList()
        val vehicleIds = response.requireBody().characters_event_list?.mapNotNull { event ->
            event.attacker_vehicle_id
        }?.distinct() ?: emptyList()

        val weaponResponse = getWeapons(weaponIds, namespace, currentLang)
        val weaponMapping = if (weaponResponse.isSuccessful) {
            weaponResponse.requireBody().map { it.id to it }.toMap()
        } else {
            emptyMap()
        }
        val vehicleResponse = getVehicles(vehicleIds, namespace, currentLang)
        val vehicleMapping = if (vehicleResponse.isSuccessful) {
            vehicleResponse.requireBody().map { it.id to it }.toMap()
        } else {
            emptyMap()
        }

        return response.process {
            formatKillList(
                character_id,
                it.characters_event_list,
                weaponMapping,
                vehicleMapping,
                namespace,
            )
        }
    }

    override suspend fun getWeaponList(
        character_id: String?,
        faction: com.cramsan.ps2link.core.models.Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<WeaponItem>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTERS_WEAPON_STAT_BY_FACTION,
            "character_id=" + character_id + "&c:join=item^show:image_path'name." + currentLang.name.lowercase() +
                "&c:join=vehicle^show:image_path'name." + currentLang.name.lowercase() + "&c:limit=10000",
            HttpNamespace.Api.WEAPON_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Weapon_list_response>(url)
        return response.process {
            formatWeapons(it.characters_weapon_stat_by_faction_list, faction, currentLang)
        }
    }

    override suspend fun getOutfitList(
        outfitTag: String,
        outfitName: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<com.cramsan.ps2link.core.models.Outfit>> {
        val query = QueryString.generateQeuryString().apply {
            if (outfitTag.length >= 3) {
                AddComparison(
                    "alias_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    outfitTag,
                )
            }
            if (outfitName.length >= 3) {
                AddComparison(
                    "name_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    outfitName,
                )
            }
            AddCommand(QueryString.QueryCommand.LIMIT, "15")
        }

        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            "",
            query,
            HttpNamespace.Api.OUTFIT_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Outfit_response>(url)
        return response.process { outfit ->
            outfit.outfit_list.map { it.toCoreModel(namespace, null, clock.now()) }
        }
    }

    override suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<com.cramsan.ps2link.core.models.Outfit> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            outfitId,
            QueryString.generateQeuryString()
                .AddCommand(QueryString.QueryCommand.RESOLVE, "leader"),
            HttpNamespace.Api.OUTFIT,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Outfit_response>(url)
        return response.process {
            it.outfit_list.first().toCoreModel(namespace, null, clock.now())
        }
    }

    override suspend fun getMemberList(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            "",
            QueryString.generateQeuryString().AddComparison(
                "outfit_id",
                QueryString.SearchModifier.EQUALS,
                outfitId,
            )
                .AddCommand(
                    QueryString.QueryCommand.RESOLVE,
                    "member_online_status,member,member_character(name,type.faction)",
                ),
            HttpNamespace.Api.OUTFIT_MEMBER_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Outfit_response>(url)
        return response.process { outfitMembers ->
            outfitMembers.outfit_list.first().members?.map {
                it.toCoreModel(
                    namespace,
                    clock.now(),
                    currentLang,
                )
            } ?: emptyList()
        }
    }

    override suspend fun getServerList(
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<World>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.WORLD,
            "",
            QueryString.generateQeuryString().AddCommand(QueryString.QueryCommand.LIMITPERDB, "20"),
            HttpNamespace.Api.SERVER_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Server_response>(url)
        return response.process {
            it.world_list
        }
    }

    override suspend fun getServerPopulation(): PS2HttpResponse<PS2> {
        // This is not an standard API call
        val url = census.generateServerPopulationRequest()
        val response = http.sendRequestWithRetry<Server_Status_response>(url)
        return response.process {
            it.ps2
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getServerMetadata(
        serverId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<WorldEvent>> {
        // The URL looks like this:
        // http://census.daybreakgames.com/get/ps2:v2/world_event?
        // world_id=17&c:limit=1&type=METAGAME&c:join=metagame_event&c:lang=en
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.WORLD_EVENT,
            "",
            QueryString.generateQeuryString().AddCommand(
                QueryString.QueryCommand.LIMIT,
                "1",
            ).AddComparison(
                "type",
                QueryString.SearchModifier.EQUALS,
                "METAGAME",
            ).AddComparison("world_id", QueryString.SearchModifier.EQUALS, serverId)
                .AddComparison(
                    "after",
                    QueryString.SearchModifier.EQUALS,
                    // Get metagame events that are newer than 2 hours
                    Clock.System.now().minus(15.hours).epochSeconds.toString(),
                ).AddCommand(QueryString.QueryCommand.JOIN, "metagame_event"),
            HttpNamespace.Api.SERVER_METADATA,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<World_event_list_response>(url)
        return response.process {
            it.world_event_list
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getStatList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<StatItem>> {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString()
                .AddCommand(QueryString.QueryCommand.RESOLVE, "stat_history")
                .AddCommand(
                    QueryString.QueryCommand.HIDE,
                    "name,battle_rank,certs,times,daily_ribbon",
                ),
            HttpNamespace.Api.STAT_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Character_list_response>(url)
        return response.process {
            val profile = it.character_list?.first()
            formatStats(profile?.stats?.stat_history)
        }
    }

    override suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Character>> {
        val url =
            census.generateGameDataRequest(
                Verb.GET,
                Collections.PS2Collection.OUTFIT_MEMBER,
                "c:limit=10000&" +
                    "c:resolve=online_status," +
                    "character(name,battle_rank,profile_id)&" +
                    "c:join=type:profile^list:0^inject_at:profile^show:name." + currentLang.name.lowercase() +
                    "^on:character.profile_id^to:profile_id&outfit_id=" + outfitId,
                HttpNamespace.Api.OUTFIT_MEMBERS_ONLINE,
                namespace.toNetworkModel(),
                currentLang,
            )
        val response = http.sendRequestWithRetry<Outfit_member_response>(url)
        return response.process { outfitMembersOnline ->
            outfitMembersOnline.outfit_member_list?.mapNotNull { it.toCoreModel(namespace) } ?: emptyList()
        }
    }

    override suspend fun getWeapons(
        weaponIds: List<String>,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<com.cramsan.ps2link.core.models.Weapon>> {
        val queryBuilder = QueryString.generateQeuryString()
        weaponIds.forEach {
            queryBuilder.AddComparison("item_id", QueryString.SearchModifier.EQUALS, it)
        }
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.ITEM,
            null,
            queryBuilder,
            HttpNamespace.Api.WEAPONS_FOR_KILL_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Item_list_response>(url)
        return response.process {
            it.item_list.map { item ->
                com.cramsan.ps2link.core.models.Weapon(
                    id = item.item_id,
                    name = item.name?.localizedName(currentLang),
                    imageUrl = item.image_path,
                )
            }
        }
    }

    override suspend fun getVehicles(
        vehicleIds: List<String>,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<Vehicle>> {
        val queryBuilder = QueryString.generateQeuryString()
        vehicleIds.forEach {
            queryBuilder.AddComparison("vehicle_id", QueryString.SearchModifier.EQUALS, it)
        }
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.VEHICLE,
            null,
            queryBuilder,
            HttpNamespace.Api.VEHICLES_FOR_KILL_LIST,
            namespace.toNetworkModel(),
            currentLang,
        )

        val response = http.sendRequestWithRetry<Vehicle_list_response>(url)
        return response.process {
            it.vehicle_list.map { item ->
                Vehicle(
                    id = item.vehicle_id,
                    name = item.name?.localizedName(currentLang),
                    imageUrl = item.image_path,
                )
            }
        }
    }

    override suspend fun getExperienceRanks(
        ranks: List<Int>,
        filterPrestige: Int?,
        faction: com.cramsan.ps2link.core.models.Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): PS2HttpResponse<List<ExperienceRank>> {
        val queryBuilder = QueryString.generateQeuryString()

        // Apply filters to select the right experience rank
        filterPrestige?.let {
            when (it) {
                0 -> {
                    queryBuilder.AddComparison(
                        "vs.title.en",
                        QueryString.SearchModifier.NOTCONTAIN,
                        "A.S.P.%20Operative",
                    )
                }
                else -> Unit
            }
        }

        ranks.forEach {
            queryBuilder.AddComparison("rank", QueryString.SearchModifier.EQUALS, it.toString())
        }
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.EXPERIENCE_RANK,
            null,
            queryBuilder,
            HttpNamespace.Api.EXPERIENCE_RANK,
            namespace.toNetworkModel(),
            null,
        )

        val response = http.sendRequestWithRetry<ExperienceRankResponse>(url)
        return response.process {
            it.experience_rank_list.map { rank ->
                val imageId = when (faction) {
                    com.cramsan.ps2link.core.models.Faction.VS -> rank.vs?.image_id
                    com.cramsan.ps2link.core.models.Faction.NC -> rank.nc?.image_id
                    com.cramsan.ps2link.core.models.Faction.TR -> rank.tr?.image_id
                    com.cramsan.ps2link.core.models.Faction.NS, com.cramsan.ps2link.core.models.Faction.UNKNOWN -> null
                }
                val imagePath = when (faction) {
                    com.cramsan.ps2link.core.models.Faction.VS -> rank.vs_image_path
                    com.cramsan.ps2link.core.models.Faction.NC -> rank.nc_image_path
                    com.cramsan.ps2link.core.models.Faction.TR -> rank.tr_image_path
                    com.cramsan.ps2link.core.models.Faction.NS, com.cramsan.ps2link.core.models.Faction.UNKNOWN -> null
                }

                ExperienceRank(
                    rank = rank.rank?.toLongOrNull(),
                    xpMax = rank.xp_max?.toLongOrNull(),
                    imageId = imageId,
                    imagePath = DBGCensus.ENDPOINT_URL + "/" + imagePath,
                )
            }
        }
    }

    companion object {
        val TAG = "DBGServiceClient"
    }
}

@OptIn(ExperimentalTime::class)
private fun CharacterProfile.toCoreModel(
    namespace: Namespace,
    lastUpdated: Instant,
    currentLang: CensusLang,
): Character {
    val server = world_id?.let {
        Server(
            worldId = it,
            namespace = namespace,
            serverName = server?.name?.localizedName(currentLang),
        )
    }
    return Character(
        characterId = character_id,
        name = name?.first,
        activeProfileId = characterClassFromString(profile_id),
        loginStatus = LoginStatus.fromString(online_status),
        certs = certs?.available_points?.toLong(),
        percentageToNextCert = certs?.percent_to_next?.toDouble()?.times(100),
        battleRank = battle_rank?.value?.toLong(),
        prestige = prestige_level?.toLongOrNull(),
        creationTime = times?.creation?.toLongOrNull()?.let {
            Instant.fromEpochSeconds(it)
        },
        sessionCount = times?.login_count?.toLongOrNull(),
        percentageToNextBattleRank = battle_rank?.percent_to_next?.toDouble(),
        outfitRank = Rank(rank, rank_ordinal?.toLongOrNull()),
        lastLogin = times?.last_login?.toLong()?.let {
            Instant.fromEpochSeconds(it)
        },
        timePlayed = times?.minutes_played?.toLong()?.let { it.minutes },
        faction = Faction.fromString(faction_id).toCoreModel(),
        server = server,
        outfit = outfit?.toCoreModel(namespace, server, lastUpdated),
        namespace = namespace,
        cached = false,
    )
}

fun CharacterFriend.toCoreModel(
    namespace: Namespace,
): Character {
    return Character(
        characterId = character_id ?: "",
        name = name?.first,
        prestige = null,
        creationTime = null,
        sessionCount = null,
        loginStatus = LoginStatus.fromString(online),
        namespace = namespace,
        cached = false,
    )
}

fun Member.toCoreModel(namespace: Namespace): Character? {
    return character_id?.let {
        Character(
            characterId = it,
            name = character?.name?.first,
            prestige = null,
            activeProfileId = characterClassFromString(character?.profile_id),
            loginStatus = LoginStatus.fromString(online_status),
            outfitRank = Rank(rank, rank_ordinal?.toLongOrNull()),
            creationTime = null,
            sessionCount = null,
            namespace = namespace,
            cached = false,
        )
    }
}

fun Outfit.toCoreModel(
    namespace: Namespace,
    server: Server?,
    lastUpdate: Instant,
): com.cramsan.ps2link.core.models.Outfit {
    return com.cramsan.ps2link.core.models.Outfit(
        id = outfit_id,
        name = name,
        tag = alias,
        faction = Faction.fromString(leader?.faction_id).toCoreModel(),
        server = server,
        timeCreated = time_created?.let { Instant.fromEpochSeconds(it.toLong()) },
        leader = leader?.let { leader ->
            leader_character_id?.let {
                Character(
                    characterId = it,
                    name = leader.name?.first,
                    prestige = null,
                    creationTime = null,
                    sessionCount = null,
                    namespace = namespace,
                    cached = false,
                )
            }
        },
        memberCount = member_count,
        namespace = namespace,
        cached = false,
        lastUpdate = lastUpdate,
    )
}

private fun formatWeapons(
    weaponList: List<WeaponStat>?,
    faction: com.cramsan.ps2link.core.models.Faction,
    currentLang: CensusLang,
): List<WeaponItem> {
    if (weaponList == null) {
        return emptyList()
    }

    val weaponMap = weaponList.groupBy { it.item_id }
    return weaponMap.map {
        val weaponId = it.key ?: return@map null
        val weaponStats = it.value

        var weaponName: String? = null
        var vehicleName: String? = null
        var weaponUrl: String? = null
        val statMapping = mutableMapOf<WeaponEventType, WeaponStatItem>()
        for (stat in weaponStats) {
            val weaponEventType =
                StatNameType.fromString(stat.stat_name)?.toWeaponEventType() ?: continue
            weaponName = stat.item_id_join_item?.name?.localizedName(currentLang)
            vehicleName = stat.vehicle_id_join_vehicle?.name?.localizedName(currentLang)

            val item = stat.item_id_join_item
            val itemImagePath = item?.image_path
            val vehicle = stat.vehicle_id_join_vehicle
            val vehicleImagePath = vehicle?.image_path

            weaponUrl = when {
                item != null -> {
                    itemImagePath
                }
                vehicle != null -> {
                    vehicleImagePath
                }
                else -> {
                    null
                }
            }

            val statTR = if (faction != com.cramsan.ps2link.core.models.Faction.TR) {
                stat.value_tr?.toLong()
            } else {
                null
            }
            val statNC = if (faction != com.cramsan.ps2link.core.models.Faction.NC) {
                stat.value_nc?.toLong()
            } else {
                null
            }
            val statVS = if (faction != com.cramsan.ps2link.core.models.Faction.VS) {
                stat.value_vs?.toLong()
            } else {
                null
            }
            statMapping[weaponEventType] = WeaponStatItem(
                mapOf(
                    com.cramsan.ps2link.core.models.Faction.TR to statTR,
                    com.cramsan.ps2link.core.models.Faction.NC to statNC,
                    com.cramsan.ps2link.core.models.Faction.VS to statVS,
                ),
            )
        }

        val kills = statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
        WeaponItem(
            weaponId = weaponId,
            weaponName = weaponName,
            vehicleName = vehicleName,
            weaponImage = DBGCensus.ENDPOINT_URL + "/" + weaponUrl,
            statMapping = statMapping,
            medalType = kills?.toMedalType(),
        )
    }.filterNotNull()
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
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

    val stat_name = "kdr"
    val all_time = (
        (kills?.all_time?.toFloatOrNull() ?: 0f) / (deaths?.all_time?.toFloatOrNull() ?: 1f)
        ).toString()
    val results = mutableListOf<Stat>()
    val kdr = Stat.newInstance(stat_name, all_time)
    kdr.setToday((kills?.getToday() ?: 0f) / (deaths?.getToday() ?: 1f))
    kdr.setThisWeek((kills?.getThisWeek() ?: 0f) / (deaths?.getThisWeek() ?: 1f))
    kdr.setThisMonth((kills?.getThisMonth() ?: 0f) / (deaths?.getThisMonth() ?: 1f))
    results.add(kdr)

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

    // TODO: Replace this with a resource
    val stat_name_sph = "Score/Hour"
    val all_time_sph = (
        (score?.all_time?.toFloatOrNull() ?: 0f) / (
            (
                time?.all_time?.toFloatOrNull()
                    ?: 1f
                ) / 3600f
            )
        ).toString()
    val sph = Stat.newInstance(stat_name_sph, all_time_sph)
    sph.setToday((score?.getToday() ?: 0f) / ((time?.getToday() ?: 3600f) / 3600f))
    sph.setThisWeek((score?.getThisWeek() ?: 0f) / ((time?.getThisWeek() ?: 3600f) / 3600f))
    sph.setThisMonth((score?.getThisMonth() ?: 0f) / ((time?.getThisMonth() ?: 3600f) / 3600f))
    results.add(sph)

    results.addAll(stats)
    return results.map {
        it.toStatItem()
    }
}

fun formatKillList(
    characterId: String,
    characterEventList: List<CharacterEvent>?,
    weaponMapping: Map<String, com.cramsan.ps2link.core.models.Weapon>,
    vehicleMapping: Map<String, Vehicle>,
    namespace: Namespace,
): List<KillEvent> {
    if (characterEventList == null) {
        return emptyList()
    }

    return characterEventList.map {
        val attackerName: String?
        val time = it.timestamp?.toLong()?.let { Instant.fromEpochSeconds(it) }
        val killType: KillType
        var eventCharacterId: String? = null
        val faction: Faction

        val weaponName: String?
        val pathUrl: String?

        if (it.attacker_weapon_id != null && !it.attacker_weapon_id.equals("0")) {
            weaponName = weaponMapping[it.attacker_weapon_id]?.name
            pathUrl = weaponMapping[it.attacker_weapon_id]?.imageUrl
        } else {
            weaponName = vehicleMapping[it.attacker_vehicle_id]?.name
            pathUrl = vehicleMapping[it.attacker_vehicle_id]?.imageUrl
        }

        val imageUrl = if (pathUrl != null) {
            DBGCensus.ENDPOINT_URL + "/" + pathUrl
        } else {
            null
        }

        if (it.attacker_character_id == characterId) {
            faction = Faction.fromString(it.character?.faction_id)
            attackerName = it.character?.name?.first
            if (it.character_id == characterId) {
                killType = KillType.SUICIDE
            } else {
                killType = KillType.KILL
                eventCharacterId = it.character_id
            }
        } else if (it.character_id == characterId) {
            faction = Faction.fromString(it.attacker?.faction_id)
            killType = KillType.KILLEDBY
            attackerName = it.attacker?.name?.first
            eventCharacterId = it.attacker_character_id
        } else {
            killType = KillType.UNKNOWN
            attackerName = null
            faction = Faction.UNKNOWN
        }

        KillEvent(
            characterId = eventCharacterId,
            namespace = namespace,
            killType = killType,
            faction = faction.toCoreModel(),
            attacker = attackerName,
            time = time,
            weaponName = weaponName,
            weaponImage = imageUrl,
        )
    }
}
