package com.cramsan.petproject.model

class Plant(scientificName: String,
            commonName: List<String>,
            imageUrl: String,
            val family: String,
            toxicityMapper: Mapper):
    Item(scientificName,
        commonName,
        imageUrl,
        toxicityMapper)