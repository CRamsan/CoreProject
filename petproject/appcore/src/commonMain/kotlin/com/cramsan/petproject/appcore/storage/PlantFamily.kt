package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

/**
 * Interface that represents a single Family entry.
 */
interface PlantFamily {
    /**
     * Unique identifier for this family.
     */
    val id: Long
    /**
     * The family name.
     */
    val family: String
    /**
     * Unique identifier for this plant.
     */
    val plantId: Long
    /**
     * Locale that this family is on.
     */
    val locale: String
}
