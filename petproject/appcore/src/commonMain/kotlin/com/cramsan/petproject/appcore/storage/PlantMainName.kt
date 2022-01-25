package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

/**
 * Interface that represents a single plant Main name.
 */
interface PlantMainName {
    /**
     * Unique identifier for this main name.
     */
    val id: Long
    /**+
     * Text representing the name.
     */
    val mainName: String
    /**
     * Unique identifier for this plant.
     */
    val plantId: Long
    /**
     * Locale that this name is on.
     */
    val locale: String
}
