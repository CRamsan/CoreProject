package com.cramsan.ps2link.appcore

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logI
import com.cramsan.framework.metrics.logMetric
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.Verb
import com.cramsan.ps2link.appcore.dbg.content.CharacterEvent
import com.cramsan.ps2link.appcore.dbg.content.CharacterFriend
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.appcore.dbg.content.Member
import com.cramsan.ps2link.appcore.dbg.content.Outfit
import com.cramsan.ps2link.appcore.dbg.content.World
import com.cramsan.ps2link.appcore.dbg.content.WorldEvent
import com.cramsan.ps2link.appcore.dbg.content.character.Stats
import com.cramsan.ps2link.appcore.dbg.content.item.Weapon
import com.cramsan.ps2link.appcore.dbg.content.response.Character_friend_list_response
import com.cramsan.ps2link.appcore.dbg.content.response.Character_list_response
import com.cramsan.ps2link.appcore.dbg.content.response.Characters_event_list_response
import com.cramsan.ps2link.appcore.dbg.content.response.Outfit_member_response
import com.cramsan.ps2link.appcore.dbg.content.response.Outfit_response
import com.cramsan.ps2link.appcore.dbg.content.response.Server_Status_response
import com.cramsan.ps2link.appcore.dbg.content.response.Server_response
import com.cramsan.ps2link.appcore.dbg.content.response.Weapon_list_response
import com.cramsan.ps2link.appcore.dbg.content.response.World_event_list_response
import com.cramsan.ps2link.appcore.dbg.content.response.server.PS2
import com.cramsan.ps2link.appcore.dbg.util.Collections
import com.cramsan.ps2link.appcore.dbg.util.QueryString
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.toDuration

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
) : DBGServiceClient {

    override suspend fun getProfile(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): CharacterProfile? {
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
            namespace,
            currentLang,
        )
        val body = sendRequestWithRetry<Character_list_response>(url)
        return body?.character_list?.first()
    }

    override suspend fun getProfiles(
        searchField: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterProfile>? {
        logI(TAG, "Downloading Profile List")
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
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Character_list_response>(url)
        return body?.character_name_list?.map { it.character_id_join_character }?.filterNotNull()
    }

    override suspend fun getFriendList(
        character_id: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<CharacterFriend>? {
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
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Character_friend_list_response>(url)
        return body?.characters_friend_list?.firstOrNull()?.friend_list
    }

    override suspend fun getKillList(
        character_id: String?,
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
                character_id!!
            )
                .AddCommand(
                    QueryString.QueryCommand.RESOLVE,
                    "character,attacker"
                ).AddCommand(QueryString.QueryCommand.LIMIT, "100")
                .AddComparison("type", QueryString.SearchModifier.EQUALS, "DEATH,KILL"),
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Characters_event_list_response>(url)
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
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Weapon_list_response>(url)
        return body?.characters_weapon_stat_by_faction_list
    }

    override suspend fun getOutfitList(
        outfitTag: String,
        outfitName: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Outfit>? {
        val query = QueryString.generateQeuryString().AddComparison(
            "alias_lower",
            QueryString.SearchModifier.STARTSWITH,
            outfitTag
        ).AddComparison(
            "name_lower",
            QueryString.SearchModifier.STARTSWITH,
            outfitName
        ).AddCommand(QueryString.QueryCommand.LIMIT, "15")

        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            "",
            query,
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Outfit_response>(url)
        return body?.outfit_list
    }

    override suspend fun getOutfit(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): Outfit? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.OUTFIT,
            outfitId,
            QueryString.generateQeuryString().AddCommand(QueryString.QueryCommand.RESOLVE, "leader"),
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Outfit_response>(url)
        return body?.outfit_list?.first()
    }

    override suspend fun getMemberList(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Member>? {
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
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Outfit_member_response>(url)
        return body?.outfit_list?.firstOrNull()?.members
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
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Server_response>(url)
        return body?.world_list
    }

    override suspend fun getServerPopulation(): PS2? {
        // This is not an standard API call
        val url = Url("https://census.daybreakgames.com/s:PS2Link/json/status/ps2")

        return sendRequestWithRetry<Server_Status_response>(url)?.ps2
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
            ).AddComparison("world_id", QueryString.SearchModifier.EQUALS, serverId!!).AddComparison(
                "after",
                QueryString.SearchModifier.EQUALS,
                // Get metagame events that are newer than 2 hours
                Clock.System.now().minus(15.hours).epochSeconds.toString()
            ).AddCommand(QueryString.QueryCommand.JOIN, "metagame_event"),
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<World_event_list_response>(url)
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
            QueryString.generateQeuryString().AddCommand(QueryString.QueryCommand.RESOLVE, "stat_history")
                .AddCommand(QueryString.QueryCommand.HIDE, "name,battle_rank,certs,times,daily_ribbon"),
            namespace,
            currentLang,
        )

        val body = sendRequestWithRetry<Character_list_response>(url)
        val profile = body?.character_list?.firstOrNull()
        return profile?.stats
    }

    override suspend fun getMembersOnline(
        outfitId: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): List<Member>? {
        val url = census.generateGameDataRequest("outfit_member?c:limit=10000&c:resolve=online_status,character(name,battle_rank,profile_id)&c:join=type:profile^list:0^inject_at:profile^show:name." + CensusLang.EN.name.toLowerCase() + "^on:character.profile_id^to:profile_id&outfit_id=" + outfitId, namespace, CensusLang.EN)
        val body = sendRequestWithRetry<Outfit_member_response>(url)
        return body?.outfit_member_list
    }

    @OptIn(ExperimentalTime::class)
    private suspend inline fun <reified T> sendRequestWithRetry(url: Url): T? {
        for (retry in 0..3) {
            delay(retry.toDuration(DurationUnit.SECONDS))
            val response = sendRequest(url, retry)

            return if (response.status.isSuccess()) {
                response.receive()
            } else if (response.status.value in 300..500) {
                null
            } else {
                continue
            }
        }

        return null
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun sendRequest(url: Url, retry: Int): HttpResponse {
        logD(TAG, "Url: $url")

        val startInstant = Clock.System.now()
        val response = http.get<HttpResponse>(url)
        val endInstant = Clock.System.now()

        val duration = endInstant.minus(startInstant).inMilliseconds
        val metadata = mapOf(
            RESPONSE_CODE to response.status.value,
            LATENCY to duration,
            RETRY to retry,
        ).mapValues { it.value.toString() }
        logMetric(TAG, "Request Completed", metadata)

        return response
    }

    companion object {
        val TAG = "DBGServiceClient"

        val RESPONSE_CODE = "RESPONSE_CODE"
        val LATENCY = "LATENCY"
        val RETRY = "RETRY"
    }
}
