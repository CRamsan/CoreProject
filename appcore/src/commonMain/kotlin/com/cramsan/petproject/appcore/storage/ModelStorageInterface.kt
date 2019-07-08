package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.Toxicity

interface ModelStorageInterface {

    fun getPlant(animalType: AnimalType, plantId: Int): Plant

    fun getPlants(animalType: AnimalType): List<Plant>

    fun getPlantMetadata(animalType: AnimalType, plantId: Int) : PlantMetadata
}