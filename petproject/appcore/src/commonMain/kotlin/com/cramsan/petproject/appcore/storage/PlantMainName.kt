package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface PlantMainName {
    val id: Long

    val mainName: String

    val plantId: Long

    val locale: String
}
