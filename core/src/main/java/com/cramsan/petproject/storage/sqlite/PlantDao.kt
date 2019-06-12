package com.cramsan.petproject.storage.sqlite

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface PlantDao {
    //@Query("SELECT * FROM Animal")
    fun getAll(): List<Plant>

    //@Query("SELECT * FROM Animal WHERE scientific_name LIKE :scientificName LIMIT 1")
    fun findByName(scientificName: String): Plant
}