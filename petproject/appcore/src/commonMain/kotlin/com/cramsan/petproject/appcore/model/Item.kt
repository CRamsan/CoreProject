package com.cramsan.petproject.appcore.model

/**
 * Base class to represent a base item.
 *
 * This class was created to decouple our implementation from
 * the use-case of plants.
 */
abstract class Item(
    /**
     * Uniquely identifies this item.
     */
    val id: Int,
    /**
     * The exact name that identifies an item.
     *
     * This could be something like the scientific name for a plant or a product code
     * for a household item.
     */
    val exactName: String,
    /**
     * Collection of common names. It is up to the implementation to define how this
     * collections is actually implemented.
     */
    val commonNames: String,
    /**
     * Url for an image to display. An empty string represents the lack of an image.
     */
    val imageUrl: String
)
