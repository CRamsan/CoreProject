package com.cramsan.petproject.appcore.model

class PlantMetadata(val id: Int,
                    val plantId: Int,
                    val animalType: AnimalType,
                    val isToxic: Boolean,
                    val description: String,
                    val source: String)