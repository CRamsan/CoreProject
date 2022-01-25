package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

/**
 * Simple plant record
 */
interface Plant {
    /**
     * Unique identifier for this plant.
     */
    val id: Long
    /**
     * Scientific name for this plant.
     */
    val scientificName: String
    /**
     * Public image URL to be displayed.
     */
    val imageUrl: String
}
