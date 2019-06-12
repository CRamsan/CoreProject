package com.cramsan.petproject.storage.sqlite

import androidx.room.Database
import androidx.room.RoomDatabase

//@Relational(entities = arrayOf(Plant::class), version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
}