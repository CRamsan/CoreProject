package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.db.Member
import com.cramsan.ps2link.db.Outfit
import com.cramsan.ps2link.db.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

class SQLDelightDAO(sqlDriver: SqlDriver) : DbgDAO {

    private var database: PS2LinkDB = PS2LinkDB(
        driver = sqlDriver,
        CharacterAdapter = Character.Adapter(
            factionIdAdapter = factionAdapter
        )
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
        factionId: Faction?,
        worldId: String?,
        outfitName: String?,
        worldName: String?,
        namespace: Namespace,
        lastUpdated: Long,
    ) {
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
            namespace.name,
            lastUpdated,
        )
    }

    override fun getAllCharactersAsFlow(): Flow<List<Character>> {
        return database.characterQueries.getAllCharacters().asFlow().mapToList()
    }

    override fun removeCharacter(
        characterId: String,
        namespace: Namespace,
    ) {
        return database.characterQueries.deleteCharacter(
            characterId,
            namespace.name,
        )
    }

    override fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character? {
        return database.characterQueries.getCharacter(characterId, namespace.name)
            .executeAsOneOrNull()
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
        database.memberQueries.deleteOutfit(outfitId, namespace.name)
    }

    override fun getCharacters(
        namespace: Namespace,
    ): List<Character> {
        return database.characterQueries.getAllCharacters().executeAsList()
    }

    override fun getAllMembers(
        outfitId: String,
        namespace: Namespace,
    ): List<Member> {
        return database.memberQueries.getAllMembers(outfitId, namespace.name).executeAsList()
    }

    override fun getOutfit(
        outfitId: String,
        namespace: Namespace,
    ): Outfit? {
        return database.outfitQueries.getOutfit(outfitId, namespace.name).executeAsOneOrNull()
    }

    override fun getAllOutfits(
        namespace: Namespace,
    ): List<Outfit> {
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
        factionId: String?,
        namespace: String,
        lastUpdated: Long,
    ) {
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
        database.outfitQueries.deleteOutfit(outfitId, namespace.name)
    }

    override fun deleteAll() {
        database.characterQueries.deleteAll()
        database.memberQueries.deleteAll()
        database.outfitQueries.deleteAll()
    }
}
