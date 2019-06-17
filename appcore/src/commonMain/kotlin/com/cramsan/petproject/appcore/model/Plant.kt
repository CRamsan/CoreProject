package com.cramsan.petproject.appcore.model

import com.cramsan.petproject.appcore.model.Item
import com.cramsan.petproject.appcore.model.Mapper

class Plant(scientificName: String,
            commonName: List<String>,
            imageUrl: String,
            val family: String,
            toxicityMapper: Mapper
):
    Item(scientificName,
        commonName,
        imageUrl,
        toxicityMapper)