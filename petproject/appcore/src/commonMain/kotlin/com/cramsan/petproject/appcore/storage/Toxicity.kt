package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

@Suppress("VariableNaming", "ConstructorParameterNaming")
interface Toxicity {
    val id: Long

    val plantId: Long

    val animalId: AnimalType

    val isToxic: ToxicityValue

    val source: String
}
