package com.cramsan.petproject.appcore.model

/**
 * Model class that contains all fields to be displayed.
 */
class PresentablePlant(
    /**
     * Id of the plant being displayed.
     */
    val plantId: Long,
    /**
     * Scientific name fo the plant being displayed.
     */
    val scientificName: String,
    /**
     * Main name to be displayed.
     */
    val mainCommonName: String,
    /**
     * Animal type for which this cell is displayed.
     */
    val animalType: AnimalType,
    /**
     * Toxicity value.
     */
    val isToxic: ToxicityValue,
)
