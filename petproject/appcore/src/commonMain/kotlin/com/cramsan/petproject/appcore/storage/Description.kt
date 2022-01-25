package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import kotlin.Long
import kotlin.String

/**
 * Representation of an entry for a Description.
 */
interface Description {
    /**
     * Unique identifier
     */
    val id: Long

    /**
     * Id of the plant that this entry matches to
     */
    val plantId: Long

    /**
     * [AnimalType] that this entry matches to
     */
    val animalId: AnimalType

    /**
     * Locale that this entry is designed for.
     */
    val locale: String

    /**
     * Textual description.
     */
    val description: String
}
