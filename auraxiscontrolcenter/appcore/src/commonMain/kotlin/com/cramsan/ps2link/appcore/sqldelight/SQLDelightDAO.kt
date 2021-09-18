package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.toDBModel
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.db.models.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class SQLDelightDAO(
    sqlDriver: SqlDriver,
    private val clock: Clock,
) : DbgDAO {

    private var database: PS2LinkDB = PS2LinkDB(
        driver = sqlDriver,
        CharacterAdapter = com.cramsan.ps2link.db.Character.Adapter(
            factionIdAdapter = factionAdapter,
            namespaceAdapter = namespaceAdapter,
            loginStatusAdapter = loginStatusAdapter,
            lastLoginAdapter = instantAdapter,
            lastUpdatedAdapter = instantAdapter,
            minutesPlayedAdapter = durationAdapter,
            activeProfileIdAdapter = characterClassAdapter,
        ),
        OutfitAdapter = com.cramsan.ps2link.db.Outfit.Adapter(
            factionIdAdapter = factionAdapter,
            namespaceAdapter = namespaceAdapter,
            lastUpdatedAdapter = instantAdapter,
            timeCreatedAdapter = instantAdapter,
        ),
    )

    @OptIn(ExperimentalTime::class)
    override fun insertCharacter(
        characterId: String,
        name: String?,
        activeProfileId: CharacterClass,
        loginStatus: LoginStatus,
        currentPoints: Long?,
        percentageToNextCert: Double?,
        percentageToNextRank: Double?,
        rank: Long?,
        outfitRank: Long?,
        lastLogin: Instant?,
        minutesPlayed: Duration?,
        factionId: Faction,
        worldId: String?,
        worldName: String?,
        outfitId: String?,
        outfitName: String?,
        namespace: Namespace,
        cached: Boolean,
        lastUpdated: Instant,
    ) {
        assertIsBackgroundThread()
        return database.characterQueries.insertCharacter(
            characterId,
            name,
            activeProfileId.toDBModel(),
            loginStatus.toDBModel(),
            currentPoints,
            percentageToNextCert,
            percentageToNextRank,
            rank,
            outfitRank,
            lastLogin,
            minutesPlayed,
            factionId.toDBModel(),
            worldId,
            worldName,
            outfitId,
            outfitName,
            namespace.toDBModel(),
            cached,
            lastUpdated,
        )
    }

    override fun insertCharacter(character: Character) {
        assertIsBackgroundThread()
        return database.characterQueries.insertCharacterInstance(character.toDBModel(clock.now()))
    }

    override fun getAllCharactersAsFlow(): Flow<List<Character>> {
        return database.characterQueries.getAllCharacters().asFlow().mapToList().map { list -> list.map { character -> character.toCoreModel() } }
    }

    override fun removeCharacter(
        characterId: String,
        namespace: Namespace,
    ) {
        assertIsBackgroundThread()
        return database.characterQueries.deleteCharacter(
            characterId,
            namespace.toDBModel(),
        )
    }

    override fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character? {
        assertIsBackgroundThread()
        return database.characterQueries.getCharacter(characterId, namespace.toDBModel())
            .executeAsOneOrNull()?.toCoreModel()
    }

    override fun getCharacterAsFlow(
        characterId: String,
        namespace: Namespace
    ): Flow<Character?> {
        return database.characterQueries.getCharacter(characterId, namespace.toDBModel()).asFlow().mapToOneOrNull().map { it?.toCoreModel() }
    }

    override fun getCharacters(): List<Character> {
        assertIsBackgroundThread()
        return database.characterQueries.getAllCharacters().executeAsList().map { it.toCoreModel() }
    }

    override fun getOutfit(
        outfitId: String,
        namespace: Namespace,
    ): Outfit? {
        assertIsBackgroundThread()
        return database.outfitQueries.getOutfit(outfitId, namespace.toDBModel()).executeAsOneOrNull()?.run {
            val server = worldId?.let {
                Server(
                    worldId = it,
                    namespace = namespace,
                    serverName = worldName,
                )
            }
            toCoreModel(server)
        }
    }

    override fun getOutfitAsFlow(outfitId: String, namespace: Namespace): Flow<Outfit?> {
        return database.outfitQueries.getOutfit(outfitId, namespace.toDBModel()).asFlow().mapToOneOrNull().map { it?.toCoreModel(null) }
    }

    override fun getAllOutfits(): List<Outfit> {
        assertIsBackgroundThread()
        return database.outfitQueries.getAllOutfits().executeAsList().map { it.toCoreModel(null) }
    }

    override fun getAllOutfitsAsFlow(): Flow<List<Outfit>> {
        return database.outfitQueries.getAllOutfits().asFlow().mapToList().map { list -> list.map { outfit -> outfit.toCoreModel(null) } }
    }

    override fun insertOutfit(
        outfitId: String,
        name: String?,
        alias: String?,
        leaderCharacterId: String?,
        leaderCharacterName: String?,
        memberCount: Long?,
        timeCreated: Instant?,
        worldId: String?,
        worldName: String?,
        factionId: Faction,
        namespace: Namespace,
        cached: Boolean,
        lastUpdated: Instant,
    ) {
        assertIsBackgroundThread()
        database.outfitQueries.insertOutfit(
            outfitId,
            name,
            alias,
            leaderCharacterId,
            leaderCharacterName,
            memberCount,
            timeCreated,
            worldId,
            worldName,
            factionId.toDBModel(),
            namespace.toDBModel(),
            cached,
            lastUpdated,
        )
    }

    override fun insertOutfit(outfit: Outfit) {
        assertIsBackgroundThread()
        return database.outfitQueries.insertOutfitInstance(outfit.toDBModel(clock.now()))
    }

    override fun removeOutfit(
        outfitId: String,
        namespace: Namespace,
    ) {
        assertIsBackgroundThread()
        database.outfitQueries.deleteOutfit(outfitId, namespace.toDBModel())
    }

    override fun deleteAll() {
        assertIsBackgroundThread()
        database.characterQueries.deleteAll()
        database.outfitQueries.deleteAll()
    }
}

@OptIn(ExperimentalTime::class)
private fun Character.toDBModel(lastUpdated: Instant): com.cramsan.ps2link.db.Character {
    return com.cramsan.ps2link.db.Character(
        id = characterId,
        name = name,
        activeProfileId = activeProfileId.toDBModel(),
        loginStatus = loginStatus.toDBModel(),
        currentPoints = certs,
        percentageToNextCert = percentageToNextCert,
        percentageToNextRank = percentageToNextBattleRank,
        rank = battleRank,
        outfitRank = null,
        lastLogin = lastLogin,
        minutesPlayed = timePlayed,
        factionId = faction.toDBModel(),
        worldId = server?.worldId,
        worldName = server?.serverName,
        outfitId = outfit?.id,
        outfitName = outfit?.name,
        namespace = namespace.toDBModel(),
        cached = cached,
        lastUpdated = lastUpdated,
    )
}

@OptIn(ExperimentalTime::class)
private fun Outfit.toDBModel(lastUpdated: Instant): com.cramsan.ps2link.db.Outfit {
    return com.cramsan.ps2link.db.Outfit(
        id = id,
        name = name,
        alias = tag,
        leaderCharacterId = leader?.characterId,
        leaderCharacterName = leader?.name,
        memberCount = memberCount.toLong(),
        timeCreated = timeCreated,
        worldId = server?.worldId,
        worldName = server?.serverName,
        factionId = faction.toDBModel(),
        namespace = namespace.toDBModel(),
        cached = cached,
        lastUpdated = lastUpdated,
    )
}

@OptIn(ExperimentalTime::class)
private fun com.cramsan.ps2link.db.Outfit.toCoreModel(server: Server?): Outfit {
    return Outfit(
        id = id,
        name = name,
        tag = alias,
        faction = factionId.toCoreModel(),
        server = server,
        timeCreated = timeCreated,
        leader = leaderCharacterId?.let {
            Character(
                characterId = it,
                name = leaderCharacterName,
                namespace = namespace.toCoreModel(),
                cached = false,
            )
        },
        memberCount = memberCount?.toInt() ?: 0,
        namespace = namespace.toCoreModel(),
        lastUpdate = lastUpdated,
        cached = cached,
    )
}

@OptIn(ExperimentalTime::class)
private fun com.cramsan.ps2link.db.Character.toCoreModel(): Character {
    val server = worldId?.let {
        Server(
            worldId = it,
            serverName = worldName,
            namespace = namespace.toCoreModel(),
            serverMetadata = null
        )
    }

    val outfit = outfitId?.let {
        Outfit(
            id = it,
            name = outfitName,
            namespace = namespace.toCoreModel(),
            cached = false,
        )
    }
    return Character(
        characterId = id,
        name = name,
        activeProfileId = activeProfileId.toCoreModel(),
        loginStatus = loginStatus.toCoreModel(),
        certs = currentPoints,
        battleRank = rank,
        percentageToNextCert = percentageToNextCert,
        percentageToNextBattleRank = percentageToNextRank,
        lastLogin = lastLogin,
        timePlayed = minutesPlayed,
        faction = factionId.toCoreModel(),
        server = server,
        outfit = outfit,
        namespace = namespace.toCoreModel(),
        cached = cached,
        lastUpdate = lastUpdated,
    )
}
