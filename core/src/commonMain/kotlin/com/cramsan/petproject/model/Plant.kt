package com.cramsan.petproject.model

class Plant(scientificName: String,
            commonName: List<String>,
            val family: String,
            toxicityMapper: Mapper):
    Item(scientificName,
        commonName,
        toxicityMapper) {
    fun title(): String {
        return exactName
    }

    fun subTitle(): String {
        return commonName.joinToString { ", " }
    }
}