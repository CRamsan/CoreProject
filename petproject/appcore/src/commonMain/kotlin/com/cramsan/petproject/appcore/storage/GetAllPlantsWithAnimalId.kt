package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

/**
 * Interface to access read a plant record.
 */
interface GetAllPlantsWithAnimalId {
    /**
     * Unique identifier for this plant.
     */
    val id: Long
    /**
     * Scientific name for this plant.
     */
    val scientificName: String
    /**
     * The main name for this plant.
     */
    val mainName: String
    /**
     * Id of the animal that this record matches to.
     */
    val animalId: AnimalType?
    /**
     * Toxicity value of this plant for the [animalId]
     */
    val isToxic: ToxicityValue?
}
