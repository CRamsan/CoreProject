package com.cramsan.ps2link.appcore.census

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.census.DBGCensus.Companion.SERVICE_ID
import com.cramsan.ps2link.appcore.characterClassFromString
import com.cramsan.ps2link.appcore.getThisMonth
import com.cramsan.ps2link.appcore.getThisWeek
import com.cramsan.ps2link.appcore.getToday
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.setThisMonth
import com.cramsan.ps2link.appcore.setThisWeek
import com.cramsan.ps2link.appcore.setToday
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.appcore.toStatItem
import com.cramsan.ps2link.appcore.toWeaponEventType
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Rank
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.core.models.WeaponStatItem
import com.cramsan.ps2link.core.models.toMedalType
import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.network.models.Verb
import com.cramsan.ps2link.network.models.content.CharacterEvent
import com.cramsan.ps2link.network.models.content.CharacterFriend
import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.network.models.content.Member
import com.cramsan.ps2link.network.models.content.Outfit
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.WorldEvent
import com.cramsan.ps2link.network.models.content.character.Day
import com.cramsan.ps2link.network.models.content.character.Month
import com.cramsan.ps2link.network.models.content.character.Stat
import com.cramsan.ps2link.network.models.content.character.Week
import com.cramsan.ps2link.network.models.content.item.StatNameType
import com.cramsan.ps2link.network.models.content.item.Weapon
import com.cramsan.ps2link.network.models.content.response.Character_friend_list_response
import com.cramsan.ps2link.network.models.content.response.Character_list_response
import com.cramsan.ps2link.network.models.content.response.Characters_event_list_response
import com.cramsan.ps2link.network.models.content.response.Outfit_member_response
import com.cramsan.ps2link.network.models.content.response.Outfit_response
import com.cramsan.ps2link.network.models.content.response.Server_Status_response
import com.cramsan.ps2link.network.models.content.response.Server_response
import com.cramsan.ps2link.network.models.content.response.Weapon_list_response
import com.cramsan.ps2link.network.models.content.response.World_event_list_response
import com.cramsan.ps2link.network.models.content.response.server.PS2
import com.cramsan.ps2link.network.models.util.Collections
import com.cramsan.ps2link.network.models.util.QueryString
import io.ktor.http.Url
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes

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
    ): Character? {
        logI(TAG, "Downloading Profile")
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString().AddCommand(
                QueryString.QueryCommand.RESOLVE,
                "outfit,world,online_status"
            )
                .AddCommand(QueryString.QueryCommand.JOIN, "type:world^inject_at:server"),
            namespace.toNetworkModel(),
            currentLang,
        )
        val body = http.sendRequestWithRetry<Character_list_response>(Url(url))
        return body?.character_list?.first()?.toCoreModel(namespace, clock.now(), currentLang)
    }

    override suspend fun getProfiles(
        searchField: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Character>? {
        logI(TAG, "Downloading Profile List")
        if (searchField.length < 3) {
            return emptyList()
        }
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER_NAME,
            "",
            QueryString.generateQeuryString()
                .AddComparison(
                    "name.first_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    searchField.toLowerCase()
                )
                .AddCommand(QueryString.QueryCommand.LIMIT, "25")
                .AddCommand(QueryString.QueryCommand.JOIN, "character"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Character_list_response>(Url(url))
        return body?.character_name_list?.mapNotNull { it.character_id_join_character?.toCoreModel(namespace, clock.now(), currentLang) }
    }

    override suspend fun getFriendList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Character>? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTERS_FRIEND,
            null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                QueryString.SearchModifier.EQUALS,
                character_id
            )
                .AddCommand(QueryString.QueryCommand.RESOLVE, "character_name"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Character_friend_list_response>(Url(url))
        return body?.characters_friend_list?.firstOrNull()?.friend_list?.map { it.toCoreModel(namespace) }
    }

    override suspend fun getKillList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<KillEvent>? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTERS_EVENT,
            null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                QueryString.SearchModifier.EQUALS,
                character_id
            )
                .AddCommand(
                    QueryString.QueryCommand.RESOLVE,
                    "character,attacker"
                ).AddCommand(QueryString.QueryCommand.LIMIT, "100")
                .AddComparison("type", QueryString.SearchModifier.EQUALS, "DEATH,KILL"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Characters_event_list_response>(Url(url))
        return formatKillList(character_id, body?.characters_event_list, namespace)
    }

    override suspend fun getWeaponList(
        character_id: String?,
        faction: com.cramsan.ps2link.core.models.Faction,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<WeaponItem>? {
        val url = census.generateGameDataRequest(
            "characters_weapon_stat_by_faction/?" +
                "character_id=" + character_id + "&c:join=item^show:image_path'name." + currentLang.name.toLowerCase() +
                "&c:join=vehicle^show:image_path'name." + currentLang.name.toLowerCase() + "&c:limit=10000",
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Weapon_list_response>(Url(url))
        return formatWeapons(body?.characters_weapon_stat_by_faction_list, faction, currentLang)
    }

    override suspend fun getOutfitList(
        outfitTag: String,
        outfitName: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<com.cramsan.ps2link.core.models.Outfit>? {
        val query = QueryString.generateQeuryString().apply {
            if (outfitTag.length >= 3) {
                AddComparison(
                    "alias_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    outfitTag
                )
            }
            if (outfitName.length >= 3) {
                AddComparison(
                    "name_lower",
                    QueryString.SearchModifier.STARTSWITH,
                    outfitName
                )
            }
            AddCommand(QueryString.QueryCommand.LIMIT, "15")
        }

        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            "",
            query,
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Outfit_response>(Url(url))
        return body?.outfit_list?.map { it.toCoreModel(namespace, null, clock.now()) }
    }

    override suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): com.cramsan.ps2link.core.models.Outfit? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            outfitId,
            QueryString.generateQeuryString()
                .AddCommand(QueryString.QueryCommand.RESOLVE, "leader"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Outfit_response>(Url(url))
        return body?.outfit_list?.first()?.toCoreModel(namespace, null, clock.now())
    }

    override suspend fun getMemberList(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Character>? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            "",
            QueryString.generateQeuryString().AddComparison(
                "outfit_id",
                QueryString.SearchModifier.EQUALS,
                outfitId
            )
                .AddCommand(
                    QueryString.QueryCommand.RESOLVE,
                    "member_online_status,member,member_character(name,type.faction)"
                ),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Outfit_member_response>(Url(url))
        return body?.outfit_list?.firstOrNull()?.members?.map { it.toCoreModel(namespace, clock.now(), currentLang) }
    }

    override suspend fun getServerList(
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<World>? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.WORLD,
            "",
            QueryString.generateQeuryString().AddCommand(QueryString.QueryCommand.LIMIT, "10"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Server_response>(Url(url))
        return body?.world_list
    }

    override suspend fun getServerPopulation(): PS2? {
        // This is not an standard API call
        val url = Url("https://census.daybreakgames.com/s:$SERVICE_ID/json/status/ps2")

        return http.sendRequestWithRetry<Server_Status_response>(url)?.ps2
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getServerMetadata(
        serverId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<WorldEvent>? {
        // The URL looks like this:
        // http://census.daybreakgames.com/get/ps2:v2/world_event?
        // world_id=17&c:limit=1&type=METAGAME&c:join=metagame_event&c:lang=en
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.WORLD_EVENT,
            "",
            QueryString.generateQeuryString().AddCommand(
                QueryString.QueryCommand.LIMIT,
                "1"
            ).AddComparison(
                "type",
                QueryString.SearchModifier.EQUALS,
                "METAGAME"
            ).AddComparison("world_id", QueryString.SearchModifier.EQUALS, serverId!!)
                .AddComparison(
                    "after",
                    QueryString.SearchModifier.EQUALS,
                    // Get metagame events that are newer than 2 hours
                    Clock.System.now().minus(15.hours).epochSeconds.toString()
                ).AddCommand(QueryString.QueryCommand.JOIN, "metagame_event"),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<World_event_list_response>(Url(url))
        return body?.world_event_list
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getStatList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<StatItem>? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString()
                .AddCommand(QueryString.QueryCommand.RESOLVE, "stat_history")
                .AddCommand(
                    QueryString.QueryCommand.HIDE,
                    "name,battle_rank,certs,times,daily_ribbon"
                ),
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Character_list_response>(Url(url))
        val profile = body?.character_list?.firstOrNull()
        return formatStats(profile?.stats?.stat_history)
    }

    override suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Character>? {
        val url =
            census.generateGameDataRequest(
                "outfit_member?c:limit=10000&c:resolve=online_status,character(name,battle_rank,profile_id)&c:join=type:profile^list:0^inject_at:profile^show:name." + CensusLang.EN.name.toLowerCase() + "^on:character.profile_id^to:profile_id&outfit_id=" + outfitId,
                namespace.toNetworkModel(),
                CensusLang.EN
            )
        val body = http.sendRequestWithRetry<Outfit_member_response>(Url(url))
        return body?.outfit_member_list?.mapNotNull { it.toCoreModel(namespace) }
    }

    companion object {
        val TAG = "DBGServiceClient"
    }
}

@OptIn(ExperimentalTime::class)
private fun CharacterProfile.toCoreModel(
    namespace: Namespace,
    lastUpdated: Instant,
    currentLang: CensusLang
): Character {
    val server = world_id?.let {
        Server(
            worldId = it,
            namespace = namespace,
            serverName = server?.name?.localizedName(currentLang)
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
        percentageToNextBattleRank = battle_rank?.percent_to_next?.toDouble(),
        outfitRank = Rank(rank, rank_ordinal?.toLongOrNull()),
        lastLogin = times?.last_login?.toLong()?.let {
            Instant.fromEpochSeconds(it)
        },
        timePlayed = times?.minutes_played?.toLong()?.minutes,
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
            activeProfileId = characterClassFromString(character?.active_profile_id),
            loginStatus = LoginStatus.fromString(online_status),
            outfitRank = Rank(rank, rank_ordinal?.toLongOrNull()),
            namespace = namespace,
            cached = false,
        )
    }
}

fun Outfit.toCoreModel(
    namespace: Namespace,
    server: Server?,
    lastUpdate: Instant
): com.cramsan.ps2link.core.models.Outfit {
    return com.cramsan.ps2link.core.models.Outfit(
        id = outfit_id,
        name = name,
        tag = alias,
        faction = Faction.fromString(leader?.faction_id).toCoreModel(),
        server = server,
        timeCreated = time_created?.let { Instant.fromEpochMilliseconds(it.toLong()) },
        leader = leader?.let { leader ->
            leader_character_id?.let {
                Character(
                    characterId = it,
                    leader.name?.first,
                    cached = false
                )
            }
        },
        memberCount = member_count,
        namespace = namespace,
        cached = false,
        lastUpdate = lastUpdate,
    )
}

private fun formatWeapons(weaponList: List<Weapon>?, faction: com.cramsan.ps2link.core.models.Faction, currentLang: CensusLang): List<WeaponItem> {
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

            val statTR = if (faction != com.cramsan.ps2link.core.models.Faction.TR) {
                stat.value_tr.toLong()
            } else {
                null
            }
            val statNC = if (faction != com.cramsan.ps2link.core.models.Faction.NC) {
                stat.value_nc.toLong()
            } else {
                null
            }
            val statVS = if (faction != com.cramsan.ps2link.core.models.Faction.VS) {
                stat.value_vs.toLong()
            } else {
                null
            }
            statMapping[weaponEventType] = WeaponStatItem(
                mapOf(
                    com.cramsan.ps2link.core.models.Faction.TR to statTR,
                    com.cramsan.ps2link.core.models.Faction.NC to statNC,
                    com.cramsan.ps2link.core.models.Faction.VS to statVS,
                )
            )
        }

        val kills = statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
        WeaponItem(
            weaponId = weaponId,
            weaponName = weaponName,
            vehicleName = vehicleName,
            weaponImage = weaponUrl,
            statMapping = statMapping,
            medalType = kills?.toMedalType()
        )
    }.filterNotNull()
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
        val time = it.timestamp?.toLong()?.let { Instant.fromEpochSeconds(it) }
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
