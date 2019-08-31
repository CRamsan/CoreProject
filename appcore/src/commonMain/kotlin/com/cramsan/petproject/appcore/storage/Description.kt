package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import kotlin.Long
import kotlin.String

interface Description {
    val id: Long

    val plantId: Long

    val animalId: AnimalType

    val locale: String

    val description: String
}
