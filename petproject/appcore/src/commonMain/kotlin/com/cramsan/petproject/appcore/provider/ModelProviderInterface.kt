package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant

interface ModelProviderInterface {

    fun isCatalogAvailable(currentTime: Long): Boolean

    suspend fun downloadCatalog(currentTime: Long): Boolean

    fun registerForCatalogEvents(listener: ModelProviderEventListenerInterface)

    fun deregisterForCatalogEvents(listener: ModelProviderEventListenerInterface)

    suspend fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant?

    suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata?

    suspend fun getPresentablePlantsCount(animalType: AnimalType, locale: String): Long

    suspend fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant>

    suspend fun getPlantsWithToxicityPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<PresentablePlant>

    suspend fun getPlantsWithToxicityFiltered(animalType: AnimalType, query: String, locale: String): List<PresentablePlant>?

    suspend fun deleteAll()
}
