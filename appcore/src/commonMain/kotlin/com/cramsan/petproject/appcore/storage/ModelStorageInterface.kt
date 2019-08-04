package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.*

interface ModelStorageInterface {

    fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant?

    fun getPlants(animalType: AnimalType, locale: String): List<Plant>

    fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String) : PlantMetadata?

    fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant>

    fun insertPlant(plant: Plant, plantMetadata: PlantMetadata, locale: String)

    fun deleteAll()
}