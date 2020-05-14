package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface Plant {
    val id: Long

    val scientificName: String

    val imageUrl: String
}
