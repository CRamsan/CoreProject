package com.cramsan.petproject.appcore.model

class Plant(id: Int,
            scientificName: String,
            val mainCommonName: String,
            commonNames: String,
            imageUrl: String,
            val family: String,
            isToxic: Boolean?) :
    Item(id,
        scientificName,
        commonNames,
        imageUrl,
        isToxic)