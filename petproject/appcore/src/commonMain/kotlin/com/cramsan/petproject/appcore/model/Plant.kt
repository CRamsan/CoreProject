package com.cramsan.petproject.appcore.model

/**
 * This class extends from [Item] by providing extra fields that fit the use-case of plants.
 */
class Plant(
    id: Int,
    scientificName: String,
    /**
     * The name that is most commonly used for this plant.
     */
    val mainCommonName: String,
    commonNames: String,
    imageUrl: String,
    /**
     * The scientific family that this plant belongs to.
     */
    val family: String,
) :
    Item(
        id,
        scientificName,
        commonNames,
        imageUrl,
    )
