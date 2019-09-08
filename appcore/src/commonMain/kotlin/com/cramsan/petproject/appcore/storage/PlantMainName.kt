package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface PlantMainName {
    val id: Long

    val mainName: String

    val plantId: Long

    val locale: String

    data class PlantMainNameImpl(
        override val id: Long,
        val main_name: String,
        val plant_id: Long,
        override val locale: String
    ) : PlantMainName {
        override val mainName: String
            get() = main_name
        override val plantId: Long
            get() = plant_id
    }
}
