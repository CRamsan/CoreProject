package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.Toxicity

interface ModelStorageInterface {

    fun getPlants(animalType: AnimalType, forceUpdate: Boolean): List<Plant>

    fun getPlants(forceUpdate: Boolean): List<Plant>

    fun getPlant(plantId: Int): Plant?

    fun getToxicity(animalType: AnimalType, plantId: Int) : Toxicity

    fun getPlantMetadata(animalType: AnimalType, plantId: Int) : PlantMetadata
}