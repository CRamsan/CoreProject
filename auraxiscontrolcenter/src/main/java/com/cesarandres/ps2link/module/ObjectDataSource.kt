package com.cesarandres.ps2link.module

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.cesarandres.ps2link.dbg.DBGCensus

import com.cesarandres.ps2link.dbg.DBGCensus.Namespace
import com.cesarandres.ps2link.dbg.content.CharacterProfile
import com.cesarandres.ps2link.dbg.content.Faction
import com.cesarandres.ps2link.dbg.content.Member
import com.cesarandres.ps2link.dbg.content.Outfit
import com.cesarandres.ps2link.dbg.content.World
import com.cesarandres.ps2link.dbg.content.character.BattleRank
import com.cesarandres.ps2link.dbg.content.character.Certs
import com.cesarandres.ps2link.dbg.content.character.Name
import com.cesarandres.ps2link.dbg.content.character.Server
import com.cesarandres.ps2link.dbg.content.character.Times
import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.cesarandres.ps2link.dbg.util.Logger
import com.cesarandres.ps2link.module.twitter.PS2Tweet

import java.util.ArrayList

/**
 * Class that retrieves information from the SQLiteManager and convert it into
 * objects that can be used by other classes.
 */
class ObjectDataSource(context: Context, private val dbgCensus: DBGCensus) {
/**
 * Constructor that requires a reference to the current context.
 *
 * @param context reference to the calling activity.
 */

    private var database: SQLiteDatabase? = null
    private val dbHelper: SQLiteManager
    private val allColumnsWorlds = arrayOf(
        SQLiteManager.WORLDS_COLUMN_ID,
        SQLiteManager.WORLDS_COLUMN_NAME,
        SQLiteManager.WORLDS_COLUMN_STATE
    )

    private val allColumnsFactions = arrayOf(
        SQLiteManager.FACTIONS_COLUMN_ID,
        SQLiteManager.FACTIONS_COLUMN_NAME,
        SQLiteManager.FACTIONS_COLUMN_CODE,
        SQLiteManager.FACTIONS_COLUMN_ICON
    )

    private val allColumnsCharacters = arrayOf(
        SQLiteManager.CHARACTERS_COLUMN_ID,
        SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST,
        SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER,
        SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID,
        SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS,
        SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
        SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
        SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE,
        SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN,
        SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED,
        SQLiteManager.CHARACTERS_COLUMN_FACTION_ID,
        SQLiteManager.CHARACTERS_COLUMN_WORLD_ID,
        SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME,
        SQLiteManager.CACHE_COLUMN_SAVES,
        SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME,
        SQLiteManager.CHARACTERS_COLUMN_NAMESPACE
    )

    private val allColumnsMembers = arrayOf(
        SQLiteManager.MEMBERS_COLUMN_ID,
        SQLiteManager.MEMBERS_COLUMN_RANK,
        SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID,
        SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS,
        SQLiteManager.MEMBERS_COLUMN_NAME,
        SQLiteManager.CACHE_COLUMN_SAVES
    )

    private val allColumnsOutfit = arrayOf(
        SQLiteManager.OUTFIT_COLUMN_ID,
        SQLiteManager.OUTFIT_COLUMN_NAME,
        SQLiteManager.OUTFIT_COLUMN_ALIAS,
        SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID,
        SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT,
        SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
        SQLiteManager.OUTFIT_COLUMN_WORDL_ID,
        SQLiteManager.OUTFIT_COLUMN_FACTION_ID,
        SQLiteManager.CACHE_COLUMN_SAVES,
        SQLiteManager.OUTFIT_COLUMN_NAMESPACE
    )

    private val allColumnsTweet = arrayOf(
        SQLiteManager.TWEETS_COLUMN_ID,
        SQLiteManager.TWEETS_COLUMN_DATE,
        SQLiteManager.TWEETS_COLUMN_USER,
        SQLiteManager.TWEETS_COLUMN_TAG,
        SQLiteManager.TWEETS_COLUMN_CONTENT,
        SQLiteManager.TWEETS_COLUMN_PICTURE,
        SQLiteManager.TWEETS_COLUMN_OWNER
    )

    /**
     * @return An arraylist with all the factions in the database
     */
    val allFactions: ArrayList<Faction>
        get() {
            val factions = ArrayList<Faction>(0)

            try {
                val cursor = database!!.query(
                    SQLiteManager.TABLE_FACTIONS_NAME,
                    allColumnsFactions,
                    null,
                    null,
                    null,
                    null,
                    null
                )
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val faction = cursorToFaction(cursor)
                    factions.add(faction)
                    cursor.moveToNext()
                }
                cursor.close()
            } catch (e: IllegalStateException) {
                Logger.log(Log.INFO, this, "Connection closed while getting all factions")
            }

            return factions
        }

    /**
     * @return an arraylist with all the worlds in the database
     */
    // Make sure to close the cursor
    val allWorlds: ArrayList<World>
        get() {
            val worlds = ArrayList<World>(0)
            try {
                val cursor = database!!.query(
                    SQLiteManager.TABLE_WORLDS_NAME,
                    allColumnsWorlds,
                    null,
                    null,
                    null,
                    null,
                    null
                )

                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val world = cursorToWorld(cursor)
                    worlds.add(world)
                    cursor.moveToNext()
                }
                cursor.close()
            } catch (e: IllegalStateException) {
                Logger.log(Log.INFO, this, "Connection closed")
            }

            return worlds
        }

    /**
     * @return returns a cursor that points to all the worlds
     */
    val allWorldsInCursor: Cursor?
        get() {
            var cursor: Cursor? = null
            try {
                cursor = database!!.query(
                    SQLiteManager.TABLE_WORLDS_NAME,
                    allColumnsWorlds,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            } catch (e: IllegalStateException) {
                Logger.log(
                    Log.INFO,
                    this,
                    "Connection closed while getting a cursor for all worlds"
                )
            }

            return cursor
        }

    init {
        dbHelper = SQLiteManager(context)
    }

    /**
     * Open the database and get it ready to retrieve information.
     *
     * @throws SQLException if there is an error while opening the database.
     */
    fun open() {
        try {
            database = dbHelper.writableDatabase
        } catch (e: Exception) {
            Logger.log(
                Log.ERROR,
                this,
                "Could not open database, database is already locked. Trying again"
            )
            dbHelper.close()
            database = dbHelper.writableDatabase
        }

    }

    /**
     * Close the database.
     */
    fun close() {

        dbHelper.close()

    }

    /**
     * Drops all the tables and creates them again.
     */
    fun reset() {
        dbHelper.onUpgrade(
            database!!,
            SQLiteManager.DATABASE_VERSION,
            SQLiteManager.DATABASE_VERSION
        )
    }

    /**
     * @param character Character to be stored in the database
     * @param temp      Boolean to set the character as a temporary or permanent entry
     * @return True if the operation was successful, false otherwise
     */
    fun insertCharacter(character: CharacterProfile, temp: Boolean): Boolean {
        val values = ContentValues()
        values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.characterId)
        values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character.name!!.first)
        values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character.name!!.first_lower)
        values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID, character.active_profile_id)
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS,
            character.certs!!.available_points
        )
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
            character.certs!!.percent_to_next
        )
        values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character.battle_rank!!.value)
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
            character.battle_rank!!.percent_to_next
        )
        values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character.times!!.last_login)
        values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character.times!!.minutes_played)
        values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID, character.faction_id)
        values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID, character.world_id)
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME,
            character.server!!.name!!.localizedName(dbgCensus.currentLang)
        )
        if (character.outfit == null) {
            values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, "NONE")
        } else {
            values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.outfit!!.name)
        }

        val target = SQLiteManager.TABLE_CHARACTERS_NAME
        if (temp) {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
        } else {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
        }

        values.put(SQLiteManager.CHARACTERS_COLUMN_NAMESPACE, character.namespace!!.name)

        var insertId: Long = -1
        try {
            insertId = database!!.insert(target, null, values)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while inserting a single character")
        }

        return insertId != -1L
    }

    /**
     * @param characterList Arraylist with all the characters that need to be stored in
     * the database
     * @param temp          Boolean to set the character as a temporary or permanent entry
     * @return the number of characters successfully stored
     */
    fun insertAllCharacters(characterList: ArrayList<CharacterProfile>, temp: Boolean): Int {
        var count = 0
        for (character in characterList) {
            if (insertCharacter(character, temp)) {
                count++
            }
        }
        return count
    }

    /**
     * @param character Character to remove from the databse
     */
    fun deleteCharacter(character: CharacterProfile) {
        val id = character.characterId
        val target = SQLiteManager.TABLE_CHARACTERS_NAME
        try {
            database!!.delete(target, SQLiteManager.CHARACTERS_COLUMN_ID + " = " + id, null)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting a single character")
        }

    }

    /**
     * @param characterId Chraracter ID of the character to retrieve
     * @return The character with the given id, null if none is found
     */
    fun getCharacter(characterId: String): CharacterProfile? {
        val target = SQLiteManager.TABLE_CHARACTERS_NAME
        var character: CharacterProfile? = null
        try {
            val cursor = database!!.query(
                target,
                allColumnsCharacters,
                SQLiteManager.CHARACTERS_COLUMN_ID + " = " + characterId,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                character = cursorToCharacterProfile(cursor)
                cursor.moveToNext()
            }
            // Make sure to close the cursor
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while retrieving a single character")
        }

        return character
    }

    /**
     * @param character Character with more current information
     * @param temp      true if the character is just a temporary one, false otherwise
     * @return the number of rows updated
     */
    fun updateCharacter(character: CharacterProfile, temp: Boolean): Int {
        val target = SQLiteManager.TABLE_CHARACTERS_NAME

        val values = ContentValues()
        values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.characterId)
        values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character.name!!.first)
        values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character.name!!.first_lower)
        values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID, character.active_profile_id)
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS,
            character.certs!!.available_points
        )
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
            character.certs!!.percent_to_next
        )
        values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character.battle_rank!!.value)
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
            character.battle_rank!!.percent_to_next
        )
        values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character.times!!.last_login)
        values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character.times!!.minutes_played)
        values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID, character.faction_id)
        values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID, character.world_id)
        if (character.outfitName != null) {
            values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.outfitName)
        } else if (character.outfit != null) {
            values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.outfit!!.name)
        }
        values.put(
            SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME,
            character.server!!.name!!.localizedName(dbgCensus.currentLang)
        )

        if (temp) {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
        } else {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
        }

        var rowsChanged = 0
        try {
            rowsChanged = database!!.update(
                target,
                values,
                SQLiteManager.CHARACTERS_COLUMN_ID + " = " + character.characterId,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while retrieving a single character")
        }

        return rowsChanged
    }

    /**
     * @param cursor cursor pointing at a character
     * @return the Character from the database
     */
    fun cursorToCharacterProfile(cursor: Cursor): CharacterProfile {
        val character = CharacterProfile()
        character.characterId = cursor.getString(0)
        var name = Name()
        name.first = cursor.getString(1)
        name.first_lower = cursor.getString(2)
        character.name = name

        character.active_profile_id = cursor.getString(3)
        val certs = Certs()
        certs.available_points = cursor.getString(4)
        certs.percent_to_next = cursor.getString(5)
        character.certs = certs

        val br = BattleRank()
        br.percent_to_next = cursor.getInt(6)
        br.value = cursor.getInt(7)
        character.battle_rank = br

        val times = Times()
        times.last_login = cursor.getString(8)
        times.minutes_played = cursor.getString(9)
        character.times = times

        character.faction_id = cursor.getString(10)
        character.world_id = cursor.getString(11)

        character.outfitName = cursor.getString(12)

        if (cursor.getInt(13) == 1) {
            character.isCached = true
        } else {
            character.isCached = false
        }

        val nameMulti = Name_Multi()
        nameMulti.en = cursor.getString(14)
        val server = Server()
        server.name = nameMulti
        character.server = server

        var namespace: String? = cursor.getString(15)
        if (namespace == null || namespace.isEmpty()) {
            namespace = Namespace.PS2PC.name
        }
        character.namespace = Namespace.valueOf(namespace)

        return character
    }

    /**
     * @param all true to retrieve all characters in database, false will
     * retrieve only the non-temporary ones
     * @return Arraylist containing all the characters found
     */
    fun getAllCharacterProfiles(all: Boolean): ArrayList<CharacterProfile> {
        val profiles = ArrayList<CharacterProfile>(0)

        try {
            var cursor: Cursor? = null
            if (all) {
                cursor = database!!.query(
                    SQLiteManager.TABLE_CHARACTERS_NAME,
                    allColumnsCharacters,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            } else {
                cursor = database!!.query(
                    SQLiteManager.TABLE_CHARACTERS_NAME,
                    allColumnsCharacters,
                    SQLiteManager.CACHE_COLUMN_SAVES + " = 1",
                    null,
                    null,
                    null,
                    null
                )
            }
            cursor!!.moveToFirst()
            while (!cursor.isAfterLast) {
                val character = cursorToCharacterProfile(cursor)
                profiles.add(character)
                cursor.moveToNext()
            }
            // Make sure to close the cursor
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all profiles")
        }

        return profiles
    }

    /**
     * @param all true to remove all characters in database, false will
     * delete only the non-temporary ones
     * @return int number of charaters deleted
     */
    fun deleteAllCharacterProfiles(all: Boolean): Int {
        var removed = 0
        try {
            if (all) {
                removed = database!!.delete(SQLiteManager.TABLE_CHARACTERS_NAME, "*", null)
            } else {
                removed = database!!.delete(
                    SQLiteManager.TABLE_CHARACTERS_NAME,
                    SQLiteManager.CACHE_COLUMN_SAVES + " = 0",
                    null
                )
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting profiles")
        }

        return removed
    }

    /**
     * @param faction Faction to be inserted to the database
     * @return true if the process was succesful, false otherwise
     */
    fun insertFaction(faction: Faction): Boolean {
        val values = ContentValues()
        values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.id)
        values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.name!!.localizedName(dbgCensus.currentLang))
        values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.code)
        values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.icon)
        var insertId: Long = -1
        try {
            insertId = database!!.insert(SQLiteManager.TABLE_CHARACTERS_NAME, null, values)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while inserting faction")
        }

        return insertId != -1L
    }

    /**
     * @param faction Faction to be deleted from the database
     */
    fun deleteFaction(faction: Faction) {
        val id = faction.id
        try {
            database!!.delete(
                SQLiteManager.TABLE_FACTIONS_NAME,
                SQLiteManager.FACTIONS_COLUMN_ID + " = " + id,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting faction")
        }

    }

    /**
     * @param cursor Cursor pointing to a faction in the database
     * @return The faction in the position of the cursor
     */
    fun cursorToFaction(cursor: Cursor): Faction {
        val faction = Faction()
        faction.id = cursor.getString(0)
        val name = Name_Multi()
        name.en = cursor.getString(1)
        faction.name = name

        faction.code = cursor.getString(2)
        faction.icon = cursor.getString(3)
        return faction
    }

    /**
     * @param factionList Arraylist containing the factions to be inserted
     * @return the number of entries added
     */
    fun insertAllFactions(factionList: ArrayList<Faction>): Int {
        var count = 0
        for (faction in factionList) {
            if (insertFaction(faction)) {
                count++
            }
        }
        return count
    }

    /**
     * @param factionId faction id of the faction to retrieve
     * @return the Faction object in the database
     */
    fun getFaction(factionId: Int): Faction? {
        var faction: Faction? = null
        try {
            val cursor = database!!.query(
                SQLiteManager.TABLE_FACTIONS_NAME,
                allColumnsFactions,
                SQLiteManager.FACTIONS_COLUMN_ID + " = " + factionId,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                faction = cursorToFaction(cursor)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting faction")
        }

        return faction
    }

    /**
     * @param faction faction already existing in the database but with more current
     * information
     * @return the number of rows changed
     */
    fun updateFaction(faction: Faction): Int {
        val values = ContentValues()
        values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.id)
        values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.name!!.localizedName(dbgCensus.currentLang))
        values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.code)
        values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.icon)
        var rowsChanged = 0
        try {
            rowsChanged = database!!.update(
                SQLiteManager.TABLE_FACTIONS_NAME,
                values,
                SQLiteManager.FACTIONS_COLUMN_ID + " = " + faction.id,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating faction")
        }

        return rowsChanged
    }

    /**
     * @param member    the member to be inserted
     * @param outfit_id id of the outfit the member belongs to
     * @param temp      true for a temporary member, false for a permanent one
     * @return true if the operation was succesful, false otherwise
     */
    fun insertMember(member: Member, outfit_id: String, temp: Boolean): Boolean {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        var insertId: Long = -1
        val values = ContentValues()
        try {
            values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.character_id)
            values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, member.online_status)
            values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.rank)
            values.put(SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID, outfit_id)
            values.put(SQLiteManager.MEMBERS_COLUMN_NAME, member.name!!.first)
            if (temp) {
                values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
            } else {
                values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
            }
            insertId = database!!.insert(target, null, values)
        } catch (e: NullPointerException) {
            Logger.log(
                Log.INFO,
                this,
                "Error while inserting member: " + member.character_id + ", probably character was deleted"
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating faction")
        }

        return insertId != -1L
    }

    /**
     * @param member member to remove from the database
     */
    fun deleteMember(member: Member) {
        val id = member.character_id
        val target = SQLiteManager.TABLE_MEMBERS_NAME

        val whereArgs = arrayOf(id)
        try {
            database!!.delete(target, SQLiteManager.MEMBERS_COLUMN_ID + " = ?", whereArgs)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting member")
        }

    }

    /**
     * @param outfit_id outfit id for which all members will be read
     * @return an arraylist with all the members for the requested outfit
     */
    fun getAllMembers(outfit_id: String): ArrayList<Member> {
        val members = ArrayList<Member>(0)
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        val whereArgs = arrayOf(outfit_id)
        try {
            val cursor = database!!.query(
                target,
                allColumnsMembers,
                SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?",
                whereArgs,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val member = cursorToMember(cursor)
                members.add(member)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all members")
        }

        return members
    }

    /**
     * @param outfit_id outfit id to read members from
     * @param index     position to start reading from
     * @param count     number of members to read
     * @return an arraylist with all the members from the requested outfit
     * inside the given range
     */
    fun getMembers(outfit_id: String, index: Int, count: Int): ArrayList<Member> {
        val members = ArrayList<Member>(0)
        val target = SQLiteManager.TABLE_MEMBERS_NAME

        try {
            val whereArgs = arrayOf(outfit_id)
            val cursor = database!!.query(
                target,
                allColumnsMembers,
                SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?",
                whereArgs,
                null,
                null,
                null,
                "LIMIT "
                        + count + " OFFSET " + index
            )

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val member = cursorToMember(cursor)
                members.add(member)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting members")
        }

        return members
    }

    /**
     * @param outfit_id   outfit id of the outfit to read from
     * @param showOffline true will read all members, false will read only online
     * members
     * @return an arraylist with all the members of the outfit that match the
     * criteria
     */
    fun countAllMembers(outfit_id: String, showOffline: Boolean): Int {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        var count = 0

        try {
            var cursor: Cursor? = null
            if (showOffline) {
                val whereArgs = arrayOf(outfit_id)
                cursor = database!!.query(
                    target,
                    allColumnsMembers,
                    SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?",
                    whereArgs,
                    null,
                    null,
                    null
                )
            } else {
                val whereArgs = arrayOf(outfit_id, "0")
                cursor = database!!.query(
                    target,
                    allColumnsMembers,
                    SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ? AND "
                            + SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS + " != ?",
                    whereArgs,
                    null,
                    null,
                    null
                )

            }

            cursor!!.moveToFirst()
            while (!cursor.isAfterLast) {
                count++
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while counting member")
        }

        return count
    }

    /**
     * @param memberList arraylist containing all the members to add to the database
     * @param outfit_id  outfit id of the outfit this members belong to
     * @param temp       true will set the member as temporary, false will set it as
     * permanent
     * @return number of entries added
     */
    fun insertAllMembers(memberList: ArrayList<Member>, outfit_id: String, temp: Boolean): Int {
        var count = 0
        for (member in memberList) {
            if (insertMember(member, outfit_id, temp)) {
                count++
            }
        }
        return count
    }

    /**
     * @param memberId id of the member to retrieve
     * @return the member with the given id
     */
    fun getMember(memberId: String): Member? {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        var member: Member? = null

        try {
            val whereArgs = arrayOf(memberId)
            val cursor = database!!.query(
                target,
                allColumnsMembers,
                SQLiteManager.MEMBERS_COLUMN_ID + " = ?",
                whereArgs,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                member = cursorToMember(cursor)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting member")
        }

        return member
    }

    /**
     * @param outfit_id   id of the outfit to read from
     * @param showOffline true for reading all members, false will only read online
     * members
     * @return a cursor that points to the members that match the given
     * parameters
     */
    fun getMembersCursor(outfit_id: String, showOffline: Boolean): Cursor? {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        var cursor: Cursor? = null
        try {
            if (showOffline) {
                val whereArgs = arrayOf(outfit_id)
                cursor = database!!.query(
                    target,
                    allColumnsMembers,
                    SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?",
                    whereArgs,
                    null,
                    null,
                    SQLiteManager.MEMBERS_COLUMN_NAME + " COLLATE NOCASE ASC"
                )
            } else {
                val whereArgs = arrayOf(outfit_id, "0")
                cursor = database!!.query(
                    target,
                    allColumnsMembers,
                    SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ? AND "
                            + SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS + " != ?",
                    whereArgs,
                    null,
                    null,
                    SQLiteManager.MEMBERS_COLUMN_NAME + " COLLATE NOCASE ASC"
                )
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting the member cursor")
        }

        return cursor
    }

    /**
     * @param member member with more current information than the one on the
     * database
     * @param temp   true will set the member as temporary, false will set it as
     * permanent
     * @return the number of rows changed
     */
    fun updateMember(member: Member, temp: Boolean): Int {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        var rowsChanged = 0
        try {
            val values = ContentValues()
            values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.character_id)
            values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, member.online_status)
            values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.rank)
            if (temp) {
                values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
            } else {
                values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
            }
            val whereArgs = arrayOf(member.character_id)
            rowsChanged = database!!.update(
                target,
                values,
                SQLiteManager.MEMBERS_COLUMN_ID + " = ?",
                whereArgs
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating a member")
        }

        return rowsChanged
    }

    /**
     * @param outfit_id id of the outfit that will have all of it's members removed
     */
    fun deleteAllMembers(outfit_id: String?) {
        val target = SQLiteManager.TABLE_MEMBERS_NAME
        try {
            val whereArgs = arrayOf(outfit_id!!)
            database!!.delete(target, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while removing members")
        }

    }

    /**
     * @param outfit outfit to be inserted into the database
     * @param temp   true will set this outfit as temporary, false will set it as
     * permanent
     * @return true if the operation was succesful, false otherwise
     */
    fun insertOutfit(outfit: Outfit, temp: Boolean): Boolean {
        val target = SQLiteManager.TABLE_OUTFITS_NAME
        val values = ContentValues()
        values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.outfit_Id)
        values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.name)
        values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.alias)
        values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, outfit.leader_character_id)
        values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, outfit.member_count)
        values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED, outfit.leader_character_id)
        values.put(SQLiteManager.OUTFIT_COLUMN_WORDL_ID, outfit.world_id)
        values.put(SQLiteManager.OUTFIT_COLUMN_FACTION_ID, outfit.faction_id)
        values.put(SQLiteManager.OUTFIT_COLUMN_NAMESPACE, outfit.namespace!!.name)
        if (temp) {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
        } else {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
        }
        var insertId: Long = -1
        try {
            insertId = database!!.insert(target, null, values)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating a member")
        }

        return insertId != -1L
    }

    /**
     * @param outfit outfit to be removed from the database
     */
    fun deleteOutfit(outfit: Outfit) {
        val id = outfit.outfit_Id
        val target = SQLiteManager.TABLE_OUTFITS_NAME
        try {
            database!!.delete(target, SQLiteManager.OUTFIT_COLUMN_ID + " = " + id, null)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting an outfit")
        }

    }

    /**
     * @param all true will remove all outfits, false will only remove the ones set
     * as temporary entries.
     */
    fun deleteAllOutfit(all: Boolean): Int {
        val target = SQLiteManager.TABLE_OUTFITS_NAME

        try {
            var cursor: Cursor? = null
            if (all) {
                cursor = database!!.query(target, allColumnsOutfit, null, null, null, null, null)
            } else {
                cursor = database!!.query(
                    target,
                    allColumnsOutfit,
                    SQLiteManager.CACHE_COLUMN_SAVES + " = 0",
                    null,
                    null,
                    null,
                    null
                )
            }

            cursor!!.moveToFirst()
            while (!cursor.isAfterLast) {
                val outfit = cursorToOutfit(cursor)
                deleteAllMembers(outfit.outfit_Id)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all outfits")
        }

        var removed = 0
        try {
            if (all) {
                removed = database!!.delete(target, "*", null)
            } else {
                removed = database!!.delete(target, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null)
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting an outfit")
        }

        return removed
    }

    /**
     * @param cursor cursor pointing at an outfit in the database
     * @return the outfit the cursor is pointing at
     */
    fun cursorToOutfit(cursor: Cursor): Outfit {
        val outfit = Outfit()
        outfit.outfit_Id = cursor.getString(0)
        outfit.name = cursor.getString(1)
        outfit.alias = cursor.getString(2)
        outfit.leader_character_id = cursor.getString(3)
        outfit.member_count = cursor.getInt(4)
        outfit.time_created = cursor.getString(5)
        outfit.world_id = cursor.getString(6)
        outfit.faction_id = cursor.getString(7)
        if (cursor.getInt(8) == 1) {
            outfit.isCached = true
        } else {
            outfit.isCached = false
        }

        var namespace: String? = cursor.getString(9)
        if (namespace == null || namespace.isEmpty()) {
            namespace = Namespace.PS2PC.name
        }
        outfit.namespace = Namespace.valueOf(namespace)

        return outfit
    }

    /**
     * @param all true will read all outfits, false will return only the non
     * temporary ones
     * @return an arraylist with all the outfits found
     */
    fun getAllOutfits(all: Boolean): ArrayList<Outfit> {
        val outfits = ArrayList<Outfit>(0)
        val target = SQLiteManager.TABLE_OUTFITS_NAME

        try {
            var cursor: Cursor? = null
            if (all) {
                cursor = database!!.query(target, allColumnsOutfit, null, null, null, null, null)
            } else {
                cursor = database!!.query(
                    target,
                    allColumnsOutfit,
                    SQLiteManager.CACHE_COLUMN_SAVES + " = 1",
                    null,
                    null,
                    null,
                    null
                )
            }

            cursor!!.moveToFirst()
            while (!cursor.isAfterLast) {
                val outfit = cursorToOutfit(cursor)
                outfits.add(outfit)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all outfits")
        }

        return outfits
    }

    /**
     * @param outfitList arraylist with outfits to be inserted to the database
     * @param temp       true will set the outfits as temporary, false will set them as
     * permanent
     * @return the number of rows changed
     */
    fun insertAllOutfits(outfitList: ArrayList<Outfit>, temp: Boolean): Int {
        var count = 0
        for (outfit in outfitList) {
            if (insertOutfit(outfit, temp)) {
                count++
            }
        }
        return count
    }

    /**
     * @param outfitId id of the outfit to retrieve
     * @return the requested Outfit
     */
    fun getOutfit(outfitId: String): Outfit? {
        val target = SQLiteManager.TABLE_OUTFITS_NAME
        var outfit: Outfit? = null

        try {
            val cursor = database!!.query(
                target,
                allColumnsOutfit,
                SQLiteManager.OUTFIT_COLUMN_ID + " = " + outfitId,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                outfit = cursorToOutfit(cursor)
                cursor.moveToNext()
            }
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting an outfits")
        }

        return outfit
    }

    /**
     * @param outfit outfit with more current information
     * @param temp   true to set it as temporary, false to set it as permanent
     * @return the number of rows changed
     */
    fun updateOutfit(outfit: Outfit, temp: Boolean): Int {
        val target = SQLiteManager.TABLE_OUTFITS_NAME

        val values = ContentValues()
        values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.outfit_Id)
        values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.name)
        values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.alias)
        values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, outfit.leader_character_id)
        values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, outfit.member_count)
        values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED, outfit.leader_character_id)
        if (temp) {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, false)
        } else {
            values.put(SQLiteManager.CACHE_COLUMN_SAVES, true)
        }

        var rowsChanged = 0
        try {
            rowsChanged = database!!.update(
                target,
                values,
                SQLiteManager.OUTFIT_COLUMN_ID + " = " + outfit.outfit_Id,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating an outfits")
        }

        return rowsChanged
    }

    /**
     * @param world world or server to be inserted to the database
     * @return true if the operation was succesful
     */
    fun insertWorld(world: World): Boolean {
        val values = ContentValues()
        values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.name!!.localizedName(dbgCensus.currentLang))
        values.put(SQLiteManager.WORLDS_COLUMN_ID, world.world_id)
        values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.state)
        var insertId: Long = -1
        try {
            insertId = database!!.insert(SQLiteManager.TABLE_WORLDS_NAME, null, values)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while inserting a world")
        }

        return insertId != -1L
    }

    /**
     * @param world world to be removed
     */
    fun deleteWorld(world: World) {
        val id = world.world_id
        try {
            database!!.delete(
                SQLiteManager.TABLE_WORLDS_NAME,
                SQLiteManager.WORLDS_COLUMN_ID + " = " + id,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while deleting a world")
        }

    }

    /**
     * @param WorldList arraylist with all worlds to be inserted
     * @return the number of entries added
     */
    fun insertAllWorlds(WorldList: ArrayList<World>): Int {
        var count = 0
        for (World in WorldList) {
            if (insertWorld(World)) {
                count++
            }
        }
        return count
    }

    /**
     * @param worldId id of the world to retrieve
     * @return world found
     */
    fun getWorld(worldId: String): World? {
        var world: World? = null
        try {
            val cursor = database!!.query(
                SQLiteManager.TABLE_WORLDS_NAME,
                allColumnsWorlds,
                SQLiteManager.WORLDS_COLUMN_ID + " = " + worldId,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                world = cursorToWorld(cursor)
                cursor.moveToNext()

            }
            // Make sure to close the cursor
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting a world")
        }

        return world
    }

    /**
     * @param world world with the most current information
     * @return the number of rows changed
     */
    fun updateWorld(world: World): Int {

        val values = ContentValues()
        values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.name!!.localizedName(dbgCensus.currentLang))
        values.put(SQLiteManager.WORLDS_COLUMN_ID, world.world_id)
        values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.state)
        var rowsChanged = 0
        try {
            rowsChanged = database!!.update(
                SQLiteManager.TABLE_WORLDS_NAME,
                values,
                SQLiteManager.WORLDS_COLUMN_ID + " = " + world.world_id,
                null
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while updating a world")
        }

        return rowsChanged
    }

    /**
     * @param cursor cursor pointing to a world
     * @return the world at the cursor's position
     */
    fun cursorToWorld(cursor: Cursor): World {
        val world = World()

        world.world_id = cursor.getString(0)
        val name = Name_Multi()
        name.en = cursor.getString(1)
        world.name = name
        world.state = cursor.getString(2)

        return world
    }

    /**
     * @param tweet the tweet to be inserted in the database
     * @param owner the person who wrote o RT the tweet
     * @return true if the operation was sucessful, false otherwise
     */
    fun insertTweet(tweet: PS2Tweet, owner: String): Boolean {
        val values = ContentValues()
        values.put(SQLiteManager.TWEETS_COLUMN_ID, tweet.id)
        values.put(SQLiteManager.TWEETS_COLUMN_USER, tweet.user!!.toString())
        values.put(SQLiteManager.TWEETS_COLUMN_DATE, tweet.date)
        values.put(SQLiteManager.TWEETS_COLUMN_CONTENT, tweet.content)
        values.put(SQLiteManager.TWEETS_COLUMN_TAG, tweet.tag)
        values.put(SQLiteManager.TWEETS_COLUMN_PICTURE, tweet.imgUrl)
        values.put(SQLiteManager.TWEETS_COLUMN_OWNER, owner)

        val target = SQLiteManager.TABLE_TWEETS_NAME
        var insertId: Long = -1
        try {
            insertId = database!!.insert(target, null, values)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while inserting a tweet")
        }

        return insertId != -1L
    }

    /**
     * @param tweetList an arraylist with all the tweets to be stored
     * @param owner     the person who wrote or RTed the tweet
     * @return the number of entries added
     */
    fun insertAllTweets(tweetList: ArrayList<PS2Tweet>, owner: String): Int {
        var count = 0
        for (tweet in tweetList) {
            if (insertTweet(tweet, owner)) {
                count++
            } else {
                return count
            }
        }
        return count
    }

    /**
     * @param tweet tweet to be removed
     */
    fun deleteTweet(tweet: PS2Tweet) {
        val id = tweet.id
        val target = SQLiteManager.TABLE_TWEETS_NAME
        try {
            database!!.delete(target, SQLiteManager.TWEETS_COLUMN_ID + " = " + id, null)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while removing a tweet")
        }

    }

    /**
     * @param older delete all the tweets older than the specified ammount.
     * @return return the number of tweets removed
     */
    fun deleteAllTweet(older: Int): Int {
        val target = SQLiteManager.TABLE_TWEETS_NAME
        var removed = 0
        try {
            removed =
                database!!.delete(target, SQLiteManager.TWEETS_COLUMN_DATE + " < " + older, null)
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while removing all tweets")
        }

        return removed
    }

    /**
     * @param users     array with users to retrieve tweets from
     * @param startDate string with the start date in unix time
     * @param endDate   string with the end date in unix time
     * @return the arraylist with all the tweets that match the criteria
     */
    fun getAllTweets(
        users: Array<String>,
        startDate: String,
        endDate: String
    ): ArrayList<PS2Tweet> {
        val tweets = ArrayList<PS2Tweet>(0)

        val betweenArgs = arrayOf(startDate, endDate)
        var cursor: Cursor? = null
        try {
            for (i in users.indices) {
                cursor = database!!.query(
                    SQLiteManager.TABLE_TWEETS_NAME,
                    allColumnsTweet,
                    SQLiteManager.TWEETS_COLUMN_OWNER + " = " + users[i]
                            + SQLiteManager.TWEETS_COLUMN_DATE + " BETWEEN ? AND ?",
                    betweenArgs,
                    null,
                    null,
                    SQLiteManager.TWEETS_COLUMN_DATE + " DESC "
                )

                cursor!!.moveToFirst()
                while (!cursor.isAfterLast) {
                    val tweet = cursorToTweet(cursor)
                    tweets.add(tweet)
                    cursor.moveToNext()
                }
                // Make sure to close the cursor
                cursor.close()
            }
        } catch (e: IllegalStateException) {
            Logger.log(
                Log.INFO,
                this,
                "Connection closed while getting all tweets for a specific date"
            )
        }

        return tweets
    }

    /**
     * @param users array with all users to retrieve tweets from
     * @return an arraylist with all the tweets for the requested users
     */
    fun getAllTweets(users: Array<String>): ArrayList<PS2Tweet> {
        val tweets = ArrayList<PS2Tweet>(0)

        var cursor: Cursor? = null

        try {
            for (i in users.indices) {
                cursor = database!!.query(
                    SQLiteManager.TABLE_TWEETS_NAME,
                    allColumnsTweet,
                    SQLiteManager.TWEETS_COLUMN_OWNER + " = ?",
                    arrayOf(users[i]),
                    null,
                    null,
                    SQLiteManager.TWEETS_COLUMN_DATE + " DESC "
                )

                cursor!!.moveToFirst()
                while (!cursor.isAfterLast) {
                    val tweet = cursorToTweet(cursor)
                    tweets.add(tweet)
                    cursor.moveToNext()
                }
                // Make sure to close the cursor
                cursor.close()
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all tweets")
        }

        return tweets
    }

    /**
     * @param users users to retrieve tweets from
     * @return the number of tweets for the requested users
     */
    fun countAllTweets(users: Array<String>): Int {
        val target = SQLiteManager.TABLE_TWEETS_NAME
        var count = 0
        var cursor: Cursor? = null
        try {
            for (i in users.indices) {
                cursor = database!!.query(
                    target,
                    allColumnsTweet,
                    SQLiteManager.TWEETS_COLUMN_OWNER + " = '" + users[i] + "'",
                    null,
                    null,
                    null,
                    null
                )
                cursor!!.moveToFirst()
                while (!cursor.isAfterLast) {
                    count++
                    cursor.moveToNext()
                }
                cursor.close()
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while counting all tweets")
        }

        return count
    }

    /**
     * @param users      array with the users to retrieve users from
     * @param pageSize   number of tweets to retrieve
     * @param pageNumber tweets will be read on sections of length pageSize. This is
     * the nth section to retrieve
     * @return arraylist with the tweets found
     */
    fun getAllTweets(users: Array<String>, pageSize: Int, pageNumber: Int): ArrayList<PS2Tweet> {
        val tweets = ArrayList<PS2Tweet>(0)

        var cursor: Cursor? = null
        try {
            for (i in users.indices) {
                cursor = database!!.query(
                    SQLiteManager.TABLE_TWEETS_NAME,
                    allColumnsTweet,
                    SQLiteManager.TWEETS_COLUMN_OWNER + " = " + users[i],
                    null,
                    null,
                    null,
                    SQLiteManager.TWEETS_COLUMN_DATE + " DESC",
                    " limit " + pageSize + " offset " + pageSize * pageNumber
                )

                cursor!!.moveToFirst()
                while (!cursor.isAfterLast) {
                    val tweet = cursorToTweet(cursor)
                    tweets.add(tweet)
                    cursor.moveToNext()
                }
                // Make sure to close the cursor
                cursor.close()
            }
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting all tweets by pages")
        }

        return tweets
    }

    /**
     * @param tweetId id of the tweet to retrieve
     * @return the tweet with the given id
     */
    fun getTweet(tweetId: String): PS2Tweet? {
        val target = SQLiteManager.TABLE_TWEETS_NAME
        val cursor = database!!.query(
            target,
            allColumnsTweet,
            SQLiteManager.TWEETS_COLUMN_ID + " = " + tweetId,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        var tweet: PS2Tweet? = null
        try {
            while (!cursor.isAfterLast) {
                tweet = cursorToTweet(cursor)
                cursor.moveToNext()
            }
            // Make sure to close the cursor
            cursor.close()
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while getting a tweet")
        }

        return tweet
    }

    /**
     * @param users the array of users to read tweets from
     * @return a cursor pointing at a list of tweets for the given users
     */
    fun getTweetCursor(users: Array<String>): Cursor? {
        val target = SQLiteManager.TABLE_TWEETS_NAME
        var cursor: Cursor? = null
        try {
            val builder = StringBuilder()
            for (i in users.indices) {
                builder.append(SQLiteManager.TWEETS_COLUMN_OWNER + " = '" + users[i] + "'")
                if (i < users.size - 1) {
                    builder.append(" OR ")
                }
            }
            cursor = database!!.query(
                target,
                allColumnsTweet,
                builder.toString(),
                null,
                null,
                null,
                SQLiteManager.TWEETS_COLUMN_DATE + " DESC"
            )
        } catch (e: IllegalStateException) {
            Logger.log(Log.INFO, this, "Connection closed while a tweet cursor")
        }

        return cursor
    }

    companion object {

        /**
         * @param cursor Cursor pointing to the somewhere in the database
         * @param index  position to move the cursor to
         * @return the cursor on the requested position
         */
        fun cursorToPosition(cursor: Cursor, index: Int): Cursor {
            cursor.moveToPosition(index)
            return cursor
        }

        /**
         * @param cursor cursor pointing to a member in the database
         * @return member that the cursor is pointing at
         */
        fun cursorToMember(cursor: Cursor): Member {
            val member = Member()
            member.character_id = cursor.getString(0)
            member.rank = cursor.getString(1)
            member.outfit_id = cursor.getString(2)
            member.online_status = cursor.getString(3)
            val name = Name()
            name.first = cursor.getString(4)
            member.name = name
            return member
        }

        /**
         * @param cursor cursor pointing at a tweet
         * @return the tweet in the cursor's position
         */
        fun cursorToTweet(cursor: Cursor): PS2Tweet {
            val tweet = PS2Tweet()
            tweet.id = cursor.getString(0)
            tweet.date = cursor.getInt(1)
            tweet.user = cursor.getString(2)
            tweet.tag = cursor.getString(3)
            tweet.content = cursor.getString(4)
            tweet.imgUrl = cursor.getString(5)
            return tweet
        }
    }

}