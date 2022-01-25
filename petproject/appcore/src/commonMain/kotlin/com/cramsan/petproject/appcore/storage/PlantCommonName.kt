package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

/**
 * Interface to represent a since Common Name entry
 */
interface PlantCommonName {
    /**
     * Unique identifier for this common name.
     */
    val id: Long
    /**+
     * Text representing the name.
     */
    val commonName: String
    /**
     * Unique identifier for this plant.
     */
    val plantId: Long
    /**
     * Locale that this name is on.
     */
    val locale: String
}
