package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

interface GetAllPlantsWithAnimalIdAll {
    val id: Long

    val scientificName: String

    val mainName: String

    val isToxic: ToxicityValue?
}
