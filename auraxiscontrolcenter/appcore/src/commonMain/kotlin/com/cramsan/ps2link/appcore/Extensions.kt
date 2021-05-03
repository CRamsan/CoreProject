package com.cramsan.ps2link.appcore

import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.db.Outfit
import com.cramsan.ps2link.db.models.Namespace
import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.network.models.content.OnlineStatus
import com.cramsan.ps2link.network.models.content.character.Stat
import com.cramsan.ps2link.network.models.content.item.StatNameType
import com.cramsan.ps2link.network.models.content.response.server.PopulationStatus
import com.cramsan.ps2link.network.models.content.world.Name_Multi
import com.cramsan.ps2link.network.models.reddit.Post
import com.cramsan.ps2link.network.models.twitter.PS2Tweet
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

/**
 * @Author cramsan
 * @created 1/17/2021
 */

@OptIn(ExperimentalTime::class)
fun CharacterProfile.toCoreModel(
    namespace: Namespace,
    lastUpdated: Long,
    currentLang: CensusLang
): com.cramsan.ps2link.core.models.Character {
    val server = world_id?.let {
        Server(
            worldId = it,
            namespace = namespace.toCoreModel(),
            serverName = server?.name?.localizedName(currentLang)
        )
    }
    return com.cramsan.ps2link.core.models.Character(
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
        faction = com.cramsan.ps2link.db.models.Faction.fromString(faction_id).toCoreModel(),
        server = server,
        outfit = outfit?.toCoreModel(namespace, server, lastUpdated),
        namespace = namespace.toCoreModel(),
        cached = false,
    )
}

fun com.cramsan.ps2link.db.models.Faction.toCoreModel() = when (this) {
    com.cramsan.ps2link.db.models.Faction.VS -> Faction.VS
    com.cramsan.ps2link.db.models.Faction.NC -> Faction.NC
    com.cramsan.ps2link.db.models.Faction.TR -> Faction.TR
    com.cramsan.ps2link.db.models.Faction.NS -> Faction.NS
    com.cramsan.ps2link.db.models.Faction.UNKNOWN -> Faction.UNKNOWN
}

fun Faction.toDBModel() = when (this) {
    Faction.VS -> com.cramsan.ps2link.db.models.Faction.VS
    Faction.NC -> com.cramsan.ps2link.db.models.Faction.NC
    Faction.TR -> com.cramsan.ps2link.db.models.Faction.TR
    Faction.NS -> com.cramsan.ps2link.db.models.Faction.NS
    Faction.UNKNOWN -> com.cramsan.ps2link.db.models.Faction.UNKNOWN
}

fun Namespace.toCoreModel() = when (this) {
    Namespace.PS2PC -> com.cramsan.ps2link.core.models.Namespace.PS2PC
    Namespace.PS2PS4US -> com.cramsan.ps2link.core.models.Namespace.PS2PS4US
    Namespace.PS2PS4EU -> com.cramsan.ps2link.core.models.Namespace.PS2PS4EU
    Namespace.UNDETERMINED -> com.cramsan.ps2link.core.models.Namespace.UNDETERMINED
}

fun com.cramsan.ps2link.db.models.LoginStatus.toCoreModel() = when (this) {
    com.cramsan.ps2link.db.models.LoginStatus.ONLINE -> LoginStatus.ONLINE
    com.cramsan.ps2link.db.models.LoginStatus.OFFLINE -> LoginStatus.OFFLINE
    com.cramsan.ps2link.db.models.LoginStatus.UNKNOWN -> LoginStatus.UNKNOWN
}

fun LoginStatus.toDBModel() = when (this) {
    LoginStatus.ONLINE -> com.cramsan.ps2link.db.models.LoginStatus.ONLINE
    LoginStatus.OFFLINE -> com.cramsan.ps2link.db.models.LoginStatus.OFFLINE
    LoginStatus.UNKNOWN -> com.cramsan.ps2link.db.models.LoginStatus.UNKNOWN
}

fun com.cramsan.ps2link.core.models.Namespace.toDBModel() = when (this) {
    com.cramsan.ps2link.core.models.Namespace.PS2PC -> Namespace.PS2PC
    com.cramsan.ps2link.core.models.Namespace.PS2PS4US -> Namespace.PS2PS4US
    com.cramsan.ps2link.core.models.Namespace.PS2PS4EU -> Namespace.PS2PS4EU
    com.cramsan.ps2link.core.models.Namespace.UNDETERMINED -> Namespace.UNDETERMINED
}

fun com.cramsan.ps2link.core.models.Namespace.toNetworkModel() = when (this) {
    com.cramsan.ps2link.core.models.Namespace.PS2PC -> com.cramsan.ps2link.network.models.Namespace.PS2PC
    com.cramsan.ps2link.core.models.Namespace.PS2PS4US -> com.cramsan.ps2link.network.models.Namespace.PS2PS4US
    com.cramsan.ps2link.core.models.Namespace.PS2PS4EU -> com.cramsan.ps2link.network.models.Namespace.PS2PS4EU
    com.cramsan.ps2link.core.models.Namespace.UNDETERMINED -> com.cramsan.ps2link.network.models.Namespace.UNDETERMINED
}

/*
fun com.cramsan.ps2link.network.models.content.Outfit.toDBModel(namespace: Namespace, lastUpdated: Long): Outfit {
    return Outfit(
        id = outfit_id,
        name = name,
        alias = alias,
        leaderCharacterId = leader_character_id,
        memberCount = member_count.toLong(),
        timeCreated = time_created?.toLong(),
        worldId = world_id?.toString(),
        worldName = wor
        factionId = Faction.NS,
        namespace = namespace,
        lastUpdated = lastUpdated,
    )
}
*/
@OptIn(ExperimentalTime::class)
fun Character.toCoreModel(): com.cramsan.ps2link.core.models.Character {
    val server = worldId?.let {
        Server(
            worldId = it,
            serverName = worldName,
            namespace = namespace.toCoreModel(),
            serverMetadata = null
        )
    }

    val outfit = outfitId?.let {
        com.cramsan.ps2link.core.models.Outfit(
            id = it,
            name = outfitName,
            namespace = namespace.toCoreModel(),
        )
    }
    return com.cramsan.ps2link.core.models.Character(
        characterId = id,
        name = name,
        activeProfileId = activeProfileId,
        loginStatus = loginStatus?.toCoreModel(),
        certs = currentPoints,
        battleRank = rank,
        percentageToNextCert = percentageToNextCert,
        percentageToNextBattleRank = percentageToNextRank,
        lastLogin = lastLogin?.let { Instant.fromEpochMilliseconds(it) },
        timePlayed = minutesPlayed?.minutes,
        faction = factionId.toCoreModel(),
        server = server,
        outfit = outfit,
        namespace = namespace.toCoreModel(),
        cached = cached,
    )
}

@OptIn(ExperimentalTime::class)
fun com.cramsan.ps2link.core.models.Character.toDBModel(lastUpdated: Long): Character {
    return Character(
        id = characterId,
        name = name,
        activeProfileId = activeProfileId,
        loginStatus = loginStatus.toDBModel(),
        currentPoints = certs,
        percentageToNextCert = percentageToNextCert,
        percentageToNextRank = percentageToNextBattleRank,
        rank = battleRank,
        outfitRank = null,
        lastLogin = lastLogin?.toEpochMilliseconds(),
        minutesPlayed = timePlayed?.inMinutes?.toLong(),
        factionId = faction.toDBModel(),
        worldId = server?.worldId,
        worldName = server?.serverName,
        outfitId = outfit?.id,
        outfitName = outfit?.name,
        namespace = namespace.toDBModel(),
        cached = true,
        lastUpdated = lastUpdated,
    )
}

fun Name_Multi.localizedName(currentLang: CensusLang): String? {
    return when (currentLang) {
        CensusLang.DE -> this.de
        CensusLang.ES -> this.es
        CensusLang.FR -> this.fr
        CensusLang.IT -> this.it
        CensusLang.TR -> this.tr
        CensusLang.EN -> this.en
    }
}

fun PopulationStatus?.toCoreModel(): Population = when (this) {
    PopulationStatus.HIGH -> Population.HIGH
    PopulationStatus.MEDIUM -> Population.MEDIUM
    PopulationStatus.LOW -> Population.LOW
    null -> Population.UNKNOWN
}

fun OnlineStatus?.toCoreModel(): ServerStatus = when (this) {
    OnlineStatus.ONLINE -> ServerStatus.ONLINE
    OnlineStatus.OFFLINE -> ServerStatus.OFFLINE
    OnlineStatus.LOCKED -> ServerStatus.LOCKED
    null -> ServerStatus.UNKNOWN
}

fun Stat.toStatItem(): StatItem {
    return StatItem(
        statName = stat_name,
        allTime = all_time?.toDouble(),
        today = getToday().toDouble(),
        thisWeek = getThisWeek().toDouble(),
        thisMonth = getThisMonth().toDouble(),
    )
}

fun com.cramsan.ps2link.network.models.content.Outfit.toCoreModel(
    namespace: Namespace,
    server: Server?,
    lastUpdated: Long
): com.cramsan.ps2link.core.models.Outfit {
    return com.cramsan.ps2link.core.models.Outfit(
        id = outfit_id,
        name = name,
        tag = alias,
        faction = com.cramsan.ps2link.db.models.Faction.fromString(leader?.faction_id).toCoreModel(),
        server = server,
        timeCreated = time_created?.let { Instant.fromEpochMilliseconds(it.toLong()) },
        leader = leader?.let { leader ->
            leader_character_id?.let {
                com.cramsan.ps2link.core.models.Character(
                    characterId = it,
                    leader.name?.first,
                    cached = false
                )
            }
        },
        memberCount = member_count,
        namespace = namespace.toCoreModel(),
    )
}

@OptIn(ExperimentalTime::class)
fun Outfit.toCoreModel(server: Server?): com.cramsan.ps2link.core.models.Outfit {
    return com.cramsan.ps2link.core.models.Outfit(
        id = id,
        name = name,
        tag = alias,
        faction = factionId.toCoreModel(),
        server = server,
        timeCreated = timeCreated?.let { Instant.fromEpochMilliseconds(it) },
        leader = leaderCharacterId?.let {
            com.cramsan.ps2link.core.models.Character(
                characterId = it,
                name = leaderCharacterName,
                cached = false,
            )
        },
        memberCount = memberCount?.toInt() ?: 0,
        namespace = namespace.toCoreModel(),
    )
}

fun com.cramsan.ps2link.core.models.Outfit.toDBModel(lastUpdated: Long): Outfit {
    return Outfit(
        id = id,
        name = name,
        alias = tag,
        leaderCharacterId = leader?.characterId,
        leaderCharacterName = leader?.name,
        memberCount = memberCount.toLong(),
        timeCreated = timeCreated?.toEpochMilliseconds(),
        worldId = server?.worldId,
        worldName = server?.serverName,
        factionId = faction.toDBModel(),
        namespace = namespace.toDBModel(),
        lastUpdated = lastUpdated,
    )
}

fun Stat.getToday(): Float {
    return day?.d01?.toFloat() ?: 0f
}

fun Stat.setToday(value: Float) {
    day?.d01 = value.toString()
}

fun Stat.getThisWeek(): Float {
    return week?.w01?.toFloat() ?: 0f
}

fun Stat.setThisWeek(value: Float) {
    week?.w01 = value.toString()
}

fun Stat.getThisMonth(): Float {
    return month?.m01?.toFloat() ?: 0f
}

fun Stat.setThisMonth(value: Float) {
    month?.m01 = value.toString()
}

fun StatNameType.toWeaponEventType(): WeaponEventType {
    return when (this) {
        StatNameType.WEAPON_KILLS -> WeaponEventType.KILLS
        StatNameType.WEAPON_VEHICLE_KILLS -> WeaponEventType.VEHICLE_KILLS
        StatNameType.WEAPON_HEADSHOTS -> WeaponEventType.HEADSHOT_KILLS
        StatNameType.WEAPON_KILLED_BY -> WeaponEventType.KILLED_BY
        StatNameType.WEAPON_DAMAGE_TAKEN -> WeaponEventType.DAMAGE_TAKEN
        StatNameType.WEAPON_DAMAGE_GIVEN -> WeaponEventType.DAMAGE_GIVEN
    }
}

fun Post.toCoreModel(): RedditPost {
    return RedditPost(
        url = url ?: "",
        title = title ?: "",
        author = author ?: "",
        label = selftext,
        upvotes = ((ups ?: 0) - (downs ?: 0)),
        comments = num_comments ?: 0,
        createdTime = created_utc?.times(1000)?.toLong() ?: 0,
        imgUr = "",
    )
}

fun PS2Tweet.toCoreModel(): com.cramsan.ps2link.core.models.PS2Tweet {
    return com.cramsan.ps2link.core.models.PS2Tweet(
        user = user ?: "",
        content = content ?: "",
        tag = tag ?: "",
        date = date ?: 0,
        imgUrl = imgUrl ?: "",
        id = id ?: "",
    )
}
