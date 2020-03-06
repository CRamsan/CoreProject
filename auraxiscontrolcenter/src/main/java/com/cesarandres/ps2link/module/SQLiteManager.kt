package com.cesarandres.ps2link.module

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.cesarandres.ps2link.dbg.DBGCensus

/**
 * This class will handle accessing and retrieving information from the local
 * database.
 *
 * @author Cesar
 */
class SQLiteManager
/**
 * @param context reference to the activity that is accessing the database.
 */
    (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_FACTIONS_TABLE)
        db.execSQL(CREATE_WORLDS_TABLE)
        db.execSQL(CREATE_OUTFITS_TABLE)
        db.execSQL(CREATE_MEMBERS_TABLE)
        db.execSQL(CREATE_CHARACTERS_TABLE)
        db.execSQL(CREATE_TWEETS_TABLE)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TWEETS_NAME")
        db.execSQL(CREATE_TWEETS_TABLE)
        if (oldVersion < 31) {
            db.execSQL("ALTER TABLE $TABLE_CHARACTERS_NAME ADD COLUMN $CHARACTERS_COLUMN_OUTFIT_NAME varchar(-1) DEFAULT '';")
        }
        if (oldVersion < 32) {
            db.execSQL("ALTER TABLE $TABLE_CHARACTERS_NAME ADD COLUMN $CHARACTERS_COLUMN_WORLD_NAME varchar(-1) DEFAULT '';")
        }
        if (oldVersion < 33) {
            db.execSQL("ALTER TABLE " + TABLE_CHARACTERS_NAME + " ADD COLUMN " + CHARACTERS_COLUMN_NAMESPACE + " varchar(-1) DEFAULT '" + DBGCensus.Namespace.PS2PC.name + "';")
            db.execSQL("ALTER TABLE " + TABLE_OUTFITS_NAME + " ADD COLUMN " + OUTFIT_COLUMN_NAMESPACE + " varchar(-1) DEFAULT '" + DBGCensus.Namespace.PS2PC.name + "';")
        }
        // onCreate(db);
    }

    companion object {

        val TABLE_WORLDS_NAME = "worlds"
        val TABLE_FACTIONS_NAME = "factions"
        val TABLE_CHARACTERS_NAME = "characters"
        val TABLE_MEMBERS_NAME = "members"
        val TABLE_OUTFITS_NAME = "outfits"
        val TABLE_TWEETS_NAME = "tweets"

        val CACHE_COLUMN_SAVES = "cached"

        val WORLDS_COLUMN_ID = "world_id"
        val WORLDS_COLUMN_STATE = "state"
        val WORLDS_COLUMN_NAME = "name"

        val FACTIONS_COLUMN_ID = "id"
        val FACTIONS_COLUMN_NAME = "name"
        val FACTIONS_COLUMN_CODE = "code"
        val FACTIONS_COLUMN_ICON = "icon"

        val CHARACTERS_COLUMN_ID = "id"
        val CHARACTERS_COLUMN_NAME_FIRST = "name_first"
        val CHARACTERS_COLUMN_NAME_FIRST_LOWER = "name_first_lower"
        val CHARACTERS_COLUMN_ACTIVE_PROFILE_ID = "active_profile_id"
        val CHARACTERS_COLUMN_CURRENT_POINTS = "current_points"
        val CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT = "percentage_to_next_cert"
        val CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK = "percentage_to_next_rank"
        val CHARACTERS_COLUMN_RANK_VALUE = "rank_value"
        val CHARACTERS_COLUMN_LAST_LOGIN = "last_login"
        val CHARACTERS_COLUMN_MINUTES_PLAYED = "minutes_played"
        val CHARACTERS_COLUMN_FACTION_ID = "faction_id"
        val CHARACTERS_COLUMN_WORLD_ID = "world_id"
        val CHARACTERS_COLUMN_OUTFIT_NAME = "outfit_name"
        val CHARACTERS_COLUMN_WORLD_NAME = "world_name"
        val CHARACTERS_COLUMN_NAMESPACE = "namespace"

        val MEMBERS_COLUMN_ID = "character_id"
        val MEMBERS_COLUMN_RANK = "rank"
        val MEMBERS_COLUMN_OUTFIT_ID = "outfit"
        val MEMBERS_COLUMN_ONLINE_STATUS = "online_status"
        val MEMBERS_COLUMN_NAME = "name"

        val OUTFIT_COLUMN_ID = "id"
        val OUTFIT_COLUMN_NAME = "name"
        val OUTFIT_COLUMN_ALIAS = "alias"
        val OUTFIT_COLUMN_LEADER_CHARACTER_ID = "leader_character_id"
        val OUTFIT_COLUMN_MEMBER_COUNT = "member_count"
        val OUTFIT_COLUMN_TIME_CREATED = "time_created"
        val OUTFIT_COLUMN_WORDL_ID = "world_id"
        val OUTFIT_COLUMN_FACTION_ID = "faction_id"
        val OUTFIT_COLUMN_NAMESPACE = "namespace"

        val TWEETS_COLUMN_DATE = "date"
        val TWEETS_COLUMN_ID = "id"
        val TWEETS_COLUMN_USER = "user"
        val TWEETS_COLUMN_TAG = "tag"
        val TWEETS_COLUMN_CONTENT = "content"
        val TWEETS_COLUMN_PICTURE = "picture"
        val TWEETS_COLUMN_OWNER = "owner"

        val DATABASE_NAME = "ps2link.db"
        val DATABASE_VERSION = 33

        private val CREATE_WORLDS_TABLE =
            ("create table " + TABLE_WORLDS_NAME + " ( " + WORLDS_COLUMN_ID + " Int, " + WORLDS_COLUMN_STATE
                    + " Int, " + WORLDS_COLUMN_NAME + " varchar(-1), " + "PRIMARY KEY (" + WORLDS_COLUMN_ID + "));")

        private val CREATE_FACTIONS_TABLE =
            ("create table " + TABLE_FACTIONS_NAME + " ( " + FACTIONS_COLUMN_ID + " Int, " + FACTIONS_COLUMN_NAME
                    + " varchar(-1), " + FACTIONS_COLUMN_CODE + " varchar(-1), " + FACTIONS_COLUMN_ICON + " Int, " + "PRIMARY KEY (" + FACTIONS_COLUMN_ID + "));")

        private val CREATE_CHARACTERS_TABLE =
            ("create table " + TABLE_CHARACTERS_NAME + " ( " + CHARACTERS_COLUMN_ID + " varchar(-1), "
                    + CHARACTERS_COLUMN_NAME_FIRST + " varchar(-1), " + CHARACTERS_COLUMN_NAME_FIRST_LOWER + " varchar(-1), " + CHARACTERS_COLUMN_ACTIVE_PROFILE_ID
                    + " Int, " + CHARACTERS_COLUMN_CURRENT_POINTS + " Int, " + CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT + " Int, "
                    + CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK + " Int, " + CHARACTERS_COLUMN_RANK_VALUE + " Int, " + CHARACTERS_COLUMN_LAST_LOGIN + " Int, "
                    + CHARACTERS_COLUMN_MINUTES_PLAYED + " Int, " + CHARACTERS_COLUMN_FACTION_ID + " varchar(-1), " + CHARACTERS_COLUMN_WORLD_ID + " varchar(-1), "
                    + CHARACTERS_COLUMN_OUTFIT_NAME + " varchar(-1), " + CACHE_COLUMN_SAVES + " Int, " + CHARACTERS_COLUMN_WORLD_NAME + " varchar(-1), " + CHARACTERS_COLUMN_NAMESPACE + " varchar(-1), " + "PRIMARY KEY (" + CHARACTERS_COLUMN_ID + "), "
                    + "FOREIGN KEY(" + CHARACTERS_COLUMN_FACTION_ID + ") REFERENCES " + TABLE_FACTIONS_NAME + "(" + FACTIONS_COLUMN_ID + "));")

        private val CREATE_MEMBERS_TABLE =
            ("create table " + TABLE_MEMBERS_NAME + " ( " + MEMBERS_COLUMN_ID + " varchar(-1), "
                    + MEMBERS_COLUMN_RANK + " varchar(-1), " + MEMBERS_COLUMN_OUTFIT_ID + " Int, " + MEMBERS_COLUMN_ONLINE_STATUS + " varchar(-1), "
                    + MEMBERS_COLUMN_NAME + " varchar(-1), " + CACHE_COLUMN_SAVES + " Int, " + "PRIMARY KEY (" + MEMBERS_COLUMN_ID + "));")

        private val CREATE_OUTFITS_TABLE =
            ("create table " + TABLE_OUTFITS_NAME + " ( " + OUTFIT_COLUMN_ID + " varchar(-1), " + OUTFIT_COLUMN_NAME
                    + " varchar(-1), " + OUTFIT_COLUMN_ALIAS + " varchar(-1), " + OUTFIT_COLUMN_LEADER_CHARACTER_ID + " varchar(-1), " + OUTFIT_COLUMN_MEMBER_COUNT
                    + " Int, " + OUTFIT_COLUMN_TIME_CREATED + " Int, " + OUTFIT_COLUMN_WORDL_ID + " Int, " + OUTFIT_COLUMN_FACTION_ID + " varchar(-1), "
                    + CACHE_COLUMN_SAVES + " Int, " + OUTFIT_COLUMN_NAMESPACE + " varchar(-1), " + "FOREIGN KEY(" + OUTFIT_COLUMN_FACTION_ID + ") REFERENCES " + TABLE_FACTIONS_NAME + "(" + FACTIONS_COLUMN_ID
                    + "), " + "FOREIGN KEY(" + OUTFIT_COLUMN_WORDL_ID + ") REFERENCES " + TABLE_WORLDS_NAME + "(" + WORLDS_COLUMN_ID + "), " + "PRIMARY KEY ("
                    + OUTFIT_COLUMN_ID + "));")

        private val CREATE_TWEETS_TABLE =
            ("create table " + TABLE_TWEETS_NAME + " ( " + TWEETS_COLUMN_ID + " varchar(-1), " + TWEETS_COLUMN_DATE
                    + " Int, " + TWEETS_COLUMN_USER + " varchar(-1), " + TWEETS_COLUMN_TAG + " varchar(-1), " + TWEETS_COLUMN_CONTENT + " varchar(-1), "
                    + TWEETS_COLUMN_PICTURE + " varchar(-1), " + TWEETS_COLUMN_OWNER + " varchar(-1), " + "PRIMARY KEY (" + TWEETS_COLUMN_ID + "));")
    }
}