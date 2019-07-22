package com.cramsan.petproject.appcore.model

class PlantMetadata(val plantId: Int,
                    val animalType: AnimalType,
                    val isToxic: Boolean,
                    val description: String,
                    val source: String)