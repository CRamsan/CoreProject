package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

interface GetPlantWithPlantIdAndAnimalId {
    val id: Long

    val scientificName: String

    val mainName: String

    val family: String

    val imageUrl: String

    val animalId: AnimalType

    val commonNames: String

    val isToxic: ToxicityValue

    val description: String

    val source: String
}
