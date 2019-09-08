package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface PlantCommonName {
    val id: Long

    val commonName: String

    val plantId: Long

    val locale: String

    data class PlantCommonNameImpl(
        override val id: Long,
        val common_name: String,
        val plant_id: Long,
        override val locale: String
    ) : PlantCommonName {
        override val commonName: String
            get() = common_name
        override val plantId: Long
            get() = plant_id
    }
}
