package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

/**
 * Interface to access read a plant record.
 * TODO: This interface seems to be unused. Remove if possible.
 */
interface GetAllPlantsWithAnimalIdAll {
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
     * Toxicity value of this plant for the [animalId]
     */
    val isToxic: ToxicityValue?
}
