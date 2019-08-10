package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.ToxicityValue
import com.squareup.sqldelight.ColumnAdapter

class ToxicityValueAdapter : ColumnAdapter<ToxicityValue, Long> {
    override fun decode(databaseValue: Long) = ToxicityValue.values()[databaseValue.toInt()]

    override fun encode(value: ToxicityValue) = value.ordinal.toLong()
}
