package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.db.Member
import com.cramsan.ps2link.db.Outfit
import com.cramsan.ps2link.db.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

class SQLDelightDAO(sqlDriver: SqlDriver) : DbgDAO {

    private var database: PS2LinkDB = PS2LinkDB(
        driver = sqlDriver,
        CharacterAdapter = Character.Adapter(
            factionIdAdapter = factionAdapter,
            namespaceAdapter = namespaceAdapter,
        ),
        OutfitAdapter = Outfit.Adapter(
            factionIdAdapter = factionAdapter,
            namespaceAdapter = namespaceAdapter,
        ),
    )

    override fun insertCharacter(
        characterId: String,
        name: String?,
        activeProfileId: Long?,
        currentPoints: Long?,
        percentageToNextCert: Double?,
        percentageToNextRank: Double?,
        rank: Long?,
        lastLogin: Long?,
        minutesPlayed: Long?,
        factionId: Faction,
        worldId: String?,
        outfitName: String?,
        worldName: String?,
        namespace: Namespace,
        cached: Boolean,
        lastUpdated: Long,
    ) {
        assertIsBackgroundThread()
        return database.characterQueries.insertCharacter(
            characterId,
            name,
            activeProfileId,
            currentPoints,
            percentageToNextCert,
            percentageToNextRank,
            rank,
            lastLogin,
            minutesPlayed,
            factionId,
            worldId,
            outfitName,
            worldName,
            namespace,
            cached,
            lastUpdated,
        )
    }

    override fun insertCharacter(character: Character) {
        assertIsBackgroundThread()
        return database.characterQueries.insertCharacterInstance(character)
    }

    override fun getAllCharactersAsFlow(): Flow<List<Character>> {
        return database.characterQueries.getAllCharacters().asFlow().mapToList()
    }

    override fun removeCharacter(
        characterId: String,
        namespace: Namespace,
    ) {
        assertIsBackgroundThread()
        return database.characterQueries.deleteCharacter(
            characterId,
            namespace,
        )
    }

    override fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character? {
        assertIsBackgroundThread()
        return database.characterQueries.getCharacter(characterId, namespace)
            .executeAsOneOrNull()
    }

    override fun getCharacterAsFlow(
        characterId: String,
        namespace: Namespace
    ): Flow<Character?> {
        return database.characterQueries.getCharacter(characterId, namespace).asFlow().mapToOneOrNull()
    }

    override fun insertMember(
        id: String,
        rank: String?,
        outfitId: String?,
        onlineStatus: String?,
        name: String?,
        namespace: Namespace,
        lastUpdated: Long,
    ) {
        assertIsBackgroundThread()
        database.memberQueries.insertMember(
            id,
            rank,
            outfitId,
            onlineStatus,
            name,
            namespace.name,
            lastUpdated
        )
    }

    override fun insertMemberList(
        memberList: List<Member>,
        lastUpdated: Long,
    ) {
        assertIsBackgroundThread()
        database.memberQueries.transaction {
            memberList.forEach {
                database.memberQueries.insertMember(
                    it.id,
                    it.rank,
                    it.outfitId,
                    it.onlineStatus,
                    it.name,
                    it.namespace,
                    lastUpdated
                )
            }
        }
    }

    override fun removeMembersFromOutfit(
        outfitId: String,
        namespace: Namespace,
    ) {
        assertIsBackgroundThread()
        database.memberQueries.deleteOutfit(outfitId, namespace.name)
    }

    override fun getCharacters(
        namespace: Namespace,
    ): List<Character> {
        assertIsBackgroundThread()
        return database.characterQueries.getAllCharacters().executeAsList()
    }

    override fun getAllMembers(
        outfitId: String,
        namespace: Namespace,
    ): List<Member> {
        assertIsBackgroundThread()
        return database.memberQueries.getAllMembers(outfitId, namespace.toSqlType()).executeAsList()
    }

    override fun getOutfit(
        outfitId: String,
        namespace: Namespace,
    ): Outfit? {
        assertIsBackgroundThread()
        return database.outfitQueries.getOutfit(outfitId, namespace).executeAsOneOrNull()
    }

    override fun getAllOutfits(
        namespace: Namespace,
    ): List<Outfit> {
        assertIsBackgroundThread()
        return database.outfitQueries.getAllOutfits().executeAsList()
    }

    override fun getAllOutfitsAsFlow(): Flow<List<Outfit>> {
        return database.outfitQueries.getAllOutfits().asFlow().mapToList()
    }

    override fun insertOutfit(
        outfitId: String,
        name: String?,
        alias: String?,
        leaderCharacterId: String?,
        memberCount: Long?,
        timeCreated: Long?,
        worldId: Long?,
        factionId: Faction,
        namespace: Namespace,
        lastUpdated: Long,
    ) {
        assertIsBackgroundThread()
        database.outfitQueries.insertOutfit(
            outfitId,
            name,
            alias,
            leaderCharacterId,
            memberCount,
            timeCreated,
            worldId,
            factionId,
            namespace,
            lastUpdated
        )
    }

    override fun removeOutfit(
        outfitId: String,
        namespace: Namespace,
    ) {
        assertIsBackgroundThread()
        database.outfitQueries.deleteOutfit(outfitId, namespace)
    }

    override fun deleteAll() {
        assertIsBackgroundThread()
        database.characterQueries.deleteAll()
        database.memberQueries.deleteAll()
        database.outfitQueries.deleteAll()
    }
}
