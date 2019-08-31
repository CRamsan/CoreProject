package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant

interface ModelProviderInterface {

    suspend fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant?

    suspend fun getPlants(animalType: AnimalType, locale: String): List<Plant>

    suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata?

    suspend fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant>

    suspend fun getPlantsWithToxicityFiltered(animalType: AnimalType, query: String, locale: String): List<PresentablePlant>?
}
