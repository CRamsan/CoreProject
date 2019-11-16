package com.cramsan.petproject.appcore.model

class PresentablePlant(
    val plantId: Long,
    val scientificName: String,
    val mainCommonName: String,
    val animalType: AnimalType,
    val isToxic: ToxicityValue
)
