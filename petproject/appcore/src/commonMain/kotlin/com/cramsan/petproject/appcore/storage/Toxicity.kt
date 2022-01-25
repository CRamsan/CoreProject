package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlin.Long
import kotlin.String

/**
 * Interface to access a toxicity entry.
 */
interface Toxicity {
    /**
     * Unique identifier for this entry.
     */
    val id: Long
    /**
     * Unique identifier for this plant.
     */
    val plantId: Long
    /**
     * Id of the animal that this record matches to.
     */
    val animalId: AnimalType
    /**
     * Toxicity value of this plant for the [animalId]
     */
    val toxic: ToxicityValue
    /**
     * Source where this information was found. This will be a URL.
     */
    val source: String
}
