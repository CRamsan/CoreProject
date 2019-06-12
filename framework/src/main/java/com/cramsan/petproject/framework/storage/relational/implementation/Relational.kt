package com.cramsan.petproject.framework.storage.relational.implementation

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cramsan.petproject.framework.storage.relational.RelationalInterface

actual class Relational actual constructor(databaseName: String) : RelationalInterface {

    override val name = databaseName
    val context: Context? = null

    override fun open() {
        val db = Room.databaseBuilder(context!!, RoomDatabase::class.java, name).build()
    }

    override fun close() {
    }
}