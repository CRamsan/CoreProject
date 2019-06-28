package com.cramsan.petproject.appcore.model

class Plant(id: Int,
            scientificName: String,
            commonNames: String,
            imageUrl: String,
            val family: String,
            toxicityMapper: Mapper?
):
    Item(id,
        scientificName,
        commonNames,
        imageUrl,
        toxicityMapper)