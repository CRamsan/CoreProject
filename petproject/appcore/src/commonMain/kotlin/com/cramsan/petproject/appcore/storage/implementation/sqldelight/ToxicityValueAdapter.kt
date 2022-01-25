package com.cramsan.petproject.appcore.storage.implementation.sqldelight

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.squareup.sqldelight.ColumnAdapter

/**
 * Adapter to convert [AnimalType] for storing it to sqldelight.
 */
class ToxicityValueAdapter : ColumnAdapter<ToxicityValue, Long> {
    override fun decode(databaseValue: Long) = ToxicityValue.values()[databaseValue.toInt()]

    override fun encode(value: ToxicityValue) = value.ordinal.toLong()
}
