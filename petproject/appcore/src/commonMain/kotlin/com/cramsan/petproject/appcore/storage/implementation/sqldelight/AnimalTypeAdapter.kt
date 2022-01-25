package com.cramsan.petproject.appcore.storage.implementation.sqldelight

import com.cramsan.petproject.appcore.model.AnimalType
import com.squareup.sqldelight.ColumnAdapter

/**
 * Adapter to convert [AnimalType] for storing it to sqldelight.
 */
class AnimalTypeAdapter : ColumnAdapter<AnimalType, Long> {
    override fun decode(databaseValue: Long) = AnimalType.values()[databaseValue.toInt()]

    override fun encode(value: AnimalType) = value.ordinal.toLong()
}
