package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface Description {
    val id: Long

    val plantId: Long

    val animalId: AnimalType

    val locale: String

    val description: String

    data class DescriptionImpl(
        override val id: Long,
        val plant_id: Long,
        val animal_id: Int,
        override val locale: String,
        override val description: String
    ) : Description {
        override val plantId: Long
            get() = plant_id
        override val animalId: AnimalType
            get() = AnimalType.values()[animal_id]
    }
}
