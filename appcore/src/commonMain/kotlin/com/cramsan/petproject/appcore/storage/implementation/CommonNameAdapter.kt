package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.squareup.sqldelight.ColumnAdapter

class CommonNameAdapter : ColumnAdapter<AnimalType, String>  {
    override fun decode(databaseValue: String) = AnimalType.valueOf(databaseValue)

    override fun encode(value: AnimalType) = value.name
}