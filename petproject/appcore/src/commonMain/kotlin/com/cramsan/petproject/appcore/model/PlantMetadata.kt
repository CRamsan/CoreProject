package com.cramsan.petproject.appcore.model

/**
 * Model that represents extra information tied to a plant and animal at the same time.
 */
class PlantMetadata(
    /**
     * Uniquely identifies a plant.
     */
    val plantId: Int,
    /**
     * The animal that this information is tied to.
     */
    val animalType: AnimalType,

    /**
     * Toxicity value for this plant and animal combination.
     */
    val isToxic: ToxicityValue,
    /**
     * Textual description for this animal and plant combination.
     */
    val description: String,
    /**
     * Url that should point to the source where this information was retrieved.
     */
    val source: String,
)
