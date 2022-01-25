package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

/**
 * Interface to access read a plant record.
 */
interface GetPlantWithPlantIdAndAnimalId {
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
     * The family name for this plant.
     */
    val family: String
    /**
     * Public image URL to be displayed.
     */
    val imageUrl: String
    /**
     * Id of the animal that this record matches to.
     */
    val animalId: AnimalType
    /**+
     * List of common names for this plant.
     */
    val commonNames: String
    /**
     * Toxicity value of this plant for the [animalId]
     */
    val isToxic: ToxicityValue
    /**
     * Textual description of this plant.
     */
    val description: String
    /**
     * Source where this information was found. This will be a URL.
     */
    val source: String
}
