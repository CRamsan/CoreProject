package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface PlantFamily {
    val id: Long

    val family: String

    val plantId: Long

    val locale: String

    data class PlantFamilyImpl(
        override val id: Long,
        override val family: String,
        val plant_id: Long,
        override val locale: String
    ) : PlantFamily {
        override val plantId: Long
            get() = plant_id
    }
}
