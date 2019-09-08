package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface Plant {
    val id: Long

    val scientificName: String

    val imageUrl: String

    data class PlantImp(
        override val id: Long,
        val scientific_name: String,
        val image_url: String
    ) : Plant {
        override val scientificName: String
            get() = scientific_name
        override val imageUrl: String
            get() = image_url
    }
}
