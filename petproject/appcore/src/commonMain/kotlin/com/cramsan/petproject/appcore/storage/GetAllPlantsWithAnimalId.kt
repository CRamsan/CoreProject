package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

interface GetAllPlantsWithAnimalId {
    val id: Long

    val scientificName: String

    val mainName: String

    val animalId: AnimalType?

    val isToxic: ToxicityValue?
}
