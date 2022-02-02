package com.cesarandres.ps2link.deprecated.module

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Outfit
import java.util.ArrayList

/**
 * Class that retrieves information from the SQLiteManager and convert it into
 * objects that can be used by other classes.
 */
class ObjectDataSource(context: Context) {
/**
     * Constructor that requires a reference to the current context.
     *
     * @param context reference to the calling activity.
     */

    private var database: SQLiteDatabase? = null
    private val dbHelper: SQLiteManager

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

    init {
        dbHelper = SQLiteManager(context)
    }

    private val TAG = "ObjectDataSource"

    /**
     * Open the database and get it ready to retrieve information.
     *
     * @throws SQLException if there is an error while opening the database.
     */
    fun open() {
        try {
            database = dbHelper.writableDatabase
        } catch (e: Exception) {
            logE(
                TAG,
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
     * @param all true to remove all characters in database, false will
     * delete only the non-temporary ones
     * @return int number of charaters deleted
     */
    fun deleteAllCharacterProfiles(all: Boolean): Int {
        var removed = 0
        try {
            if (all) {
                removed = database!!.delete(SQLiteManager.TABLE_CHARACTERS_NAME, null, null)
            } else {
                removed = database!!.delete(
                    SQLiteManager.TABLE_CHARACTERS_NAME,
                    SQLiteManager.CACHE_COLUMN_SAVES + " = 0",
                    null
                )
            }
        } catch (e: IllegalStateException) {
            logE(
                TAG,
                "Connection closed while deleting profiles"
            )
        }

        return removed
    }

    /**
     * @param cursor cursor pointing at a character
     * @return the Character from the database
     */
    fun cursorToCharacterProfile(cursor: Cursor): Character {
        val character_id = cursor.getString(0)
        val name = cursor.getString(1)
        var namespaceString: String? = cursor.getString(15)
        if (namespaceString == null || namespaceString.isEmpty()) {
            namespaceString = Namespace.PS2PC.name
        }
        val cached = cursor.getInt(13) == 1
        val namespace = Namespace.valueOf(namespaceString)

        return Character(
            characterId = character_id,
            name = name,
            prestige = null,
            creationTime = null,
            sessionCount = null,
            namespace = namespace.toCoreModel(),
            cached = cached,
        )
    }

    /**
     * @param all true to retrieve all characters in database, false will
     * retrieve only the non-temporary ones
     * @return Arraylist containing all the characters found
     */
    fun getAllCharacterProfiles(all: Boolean): List<Character> {
        val profiles = mutableListOf<Character>()

        try {
            val cursor: Cursor?
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
            logE(TAG, "Connection closed while getting all profiles")
        }

        return profiles
    }

    /**
     * @param cursor cursor pointing at an outfit in the database
     * @return the outfit the cursor is pointing at
     */
    fun cursorToOutfit(cursor: Cursor): Outfit {
        val outfitId = cursor.getString(0)
        val name = cursor.getString(1)
        val cached = cursor.getInt(8) == 1

        var namespaceString: String? = cursor.getString(9)
        if (namespaceString == null || namespaceString.isEmpty()) {
            namespaceString = Namespace.PS2PC.name
        }
        val namespace = Namespace.valueOf(namespaceString).toCoreModel()

        return Outfit(
            id = outfitId,
            name = name,
            cached = cached,
            namespace = namespace,
        )
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
            val cursor: Cursor?
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
            logE(TAG, "Connection closed while getting all outfits")
        }

        return outfits
    }

    /**
     * @param all true will remove all outfits, false will only remove the ones set
     * as temporary entries.
     */
    fun deleteAllOutfit(all: Boolean): Int {
        val target = SQLiteManager.TABLE_OUTFITS_NAME

        var removed = 0
        try {
            if (all) {
                removed = database!!.delete(target, null, null)
            } else {
                removed = database!!.delete(target, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null)
            }
        } catch (e: IllegalStateException) {
            logE(TAG, "Connection closed while deleting an outfit")
        }

        return removed
    }

    companion object {

        /**
         * @param cursor Cursor pointing to the somewhere in the database
         * @param index position to move the cursor to
         * @return the cursor on the requested position
         */
        fun cursorToPosition(cursor: Cursor, index: Int): Cursor {
            cursor.moveToPosition(index)
            return cursor
        }
    }
}
