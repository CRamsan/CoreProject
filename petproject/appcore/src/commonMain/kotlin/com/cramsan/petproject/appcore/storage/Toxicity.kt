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

    data class ToxicityImpl(
        override val id: Long,
        val plant_id: Long,
        val animal_id: Int,
        val is_toxic: Int,
        override val source: String
    ) : Toxicity {
        override val plantId: Long
            get() = plant_id
        override val animalId: AnimalType
            get() = AnimalType.values()[animal_id.toInt()]
        override val isToxic: ToxicityValue
            get() = ToxicityValue.values()[is_toxic]
    }
}
