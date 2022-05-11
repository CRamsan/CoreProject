package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import kotlinx.coroutines.flow.Flow

/**
 * Interface to define CRUD operations for all entities that are
 * needed for this app.
 */
@Suppress("TooManyFunctions")
interface ModelStorageInterface {

    /**
     * Insert a [plant] into storage.
     */
    fun insertPlant(plant: Plant)

    /**
     * Insert a [list] of plants into storage.
     */
    fun insertPlantList(list: List<Plant>)

    /**
     * Returns a [Plant] that matches the provided [scientificName].
     */
    fun getPlant(scientificName: String): Plant?

    /**
     * Returns a list of all [Plant] in storage.
     */
    fun getPlants(): List<Plant>

    /**
     * Returns a count of all [Plant] in storage.
     */
    fun getPlantCount(): Long

    /**
     * Insert a [plantMainName] into storage.
     */
    fun insertPlantMainName(plantMainName: PlantMainName)

    /**
     * Insert a [list] of [PlantMainName] into storage.
     */
    fun insertPlantMainNameList(list: List<PlantMainName>)

    /**
     * Search for an instance of [PlantMainName] that matches the [plantId] and [locale].
     */
    fun getPlantMainName(plantId: Long, locale: String): PlantMainName?

    /**
     * Returns a list of all [PlantMainName] currently in storage.
     */
    fun getPlantsMainName(): List<PlantMainName>

    /**
     * Insert a [PlantCommonName] into storage.
     */
    fun insertPlantCommonName(plantCommonName: PlantCommonName)

    /**
     * Insert a [list] of [PlantCommonName] into storage.
     */
    fun insertPlantCommonNameList(list: List<PlantCommonName>)

    /**
     * Returns a list of all [PlantCommonName] currently in storage.
     */
    fun getPlantsCommonNames(): List<PlantCommonName>

    /**
     * Insert a [PlantFamily] into storage.
     */
    fun insertPlantFamily(plantFamily: PlantFamily)

    /**
     * Insert a [list] of [PlantFamily] into storage.
     */
    fun insertPlantFamilyList(list: List<PlantFamily>)

    /**
     * Search for an instance of [PlantFamily] that matches the [plantId] and [locale].
     */
    fun getPlantFamily(plantId: Long, locale: String): PlantFamily?

    /**
     * Returns a list of all [PlantFamily] currently in storage.
     */
    fun getPlantsFamily(): List<PlantFamily>

    /**
     * Insert a [Description] into storage.
     */
    fun insertDescription(description: Description)

    /**
     * Insert a [list] of [Description] into storage.
     */
    fun insertDescriptionList(list: List<Description>)

    /**
     * Returns a list of all [Description] currently in storage.
     */
    fun getDescription(): List<Description>

    /**
     * Insert a [Toxicity] into storage.
     */
    fun insertToxicity(toxicity: Toxicity)

    /**
     * Insert a [list] of [Toxicity] into storage.
     */
    fun insertToxicityList(list: List<Toxicity>)

    /**
     * Search for an instance of [Toxicity] that matches the [plantId] and [animalType].
     */
    fun getToxicity(plantId: Long, animalType: AnimalType): Toxicity?

    /**
     * Returns a list of all [Toxicity] currently in storage.
     */
    fun getToxicity(): List<Toxicity>

    /**
     * Return a [GetPlantWithPlantIdAndAnimalId] that matches the [animalType], [plantId] and [locale].
     */
    fun getCustomPlantEntry(animalType: AnimalType, plantId: Int, locale: String): GetPlantWithPlantIdAndAnimalId?

    /**
     * Returns a list of all [GetAllPlantsWithAnimalId] that match the [animalType] and [locale].
     */
    fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    /**
     * Returns a flow that emits the list of all [GetAllPlantsWithAnimalId] that match the [animalType] and [locale].
     */
    fun getCustomPlantsEntriesFlow(animalType: AnimalType, locale: String): Flow<List<GetAllPlantsWithAnimalId>>

    /**
     * Returns a list of [GetAllPlantsWithAnimalId] that match the [animalType] and [locale]. The result is paginated
     * by using [limit] and [offset].
     */
    fun getCustomPlantEntriesPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long,
    ): List<GetAllPlantsWithAnimalId>

    /**
     * Delete all entries in storage.
     */
    fun deleteAll()
}
