package com.cramsan.ps2link.appcore.census

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.census.DBGCensus.Companion.SERVICE_ID
import com.cramsan.ps2link.appcore.localizedName
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Rank
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.network.models.Verb
import com.cramsan.ps2link.network.models.content.CharacterEvent
import com.cramsan.ps2link.network.models.content.CharacterFriend
import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.network.models.content.Member
import com.cramsan.ps2link.network.models.content.Outfit
import com.cramsan.ps2link.network.models.content.World
import com.cramsan.ps2link.network.models.content.WorldEvent
import com.cramsan.ps2link.network.models.content.character.Stats
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
    ): List<CharacterEvent>? {
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
        return body?.characters_event_list
    }

    override suspend fun getWeaponList(
        character_id: String?,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Weapon>? {
        val url = census.generateGameDataRequest(
            "characters_weapon_stat_by_faction/?" +
                "character_id=" + character_id + "&c:join=item^show:image_path'name." + currentLang.name.toLowerCase() +
                "&c:join=vehicle^show:image_path'name." + currentLang.name.toLowerCase() + "&c:limit=10000",
            namespace.toNetworkModel(),
            currentLang,
        )

        val body = http.sendRequestWithRetry<Weapon_list_response>(Url(url))
        return body?.characters_weapon_stat_by_faction_list
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
    ): Stats? {
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
        return profile?.stats
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
        activeProfileId = profile_id,
        loginStatus = LoginStatus.fromString(online_status),
        certs = certs?.available_points?.toLong(),
        percentageToNextCert = certs?.percent_to_next?.toDouble()?.times(100),
        battleRank = battle_rank?.value?.toLong(),
        percentageToNextBattleRank = battle_rank?.percent_to_next?.toDouble(),
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
            activeProfileId = character?.active_profile_id,
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
        lastUpdate = lastUpdate,
    )
}
