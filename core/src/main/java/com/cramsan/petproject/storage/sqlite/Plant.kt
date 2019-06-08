package com.cramsan.petproject.storage.sqlite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal class Plant(
    @PrimaryKey val scientificName: String,
    @ColumnInfo(name = "common_name") val commonName: List<String>,
    @ColumnInfo(name = "family") val family: String
)