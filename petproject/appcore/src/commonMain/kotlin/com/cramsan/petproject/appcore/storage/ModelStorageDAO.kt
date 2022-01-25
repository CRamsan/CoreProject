package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import kotlinx.coroutines.flow.Flow

/**
 * This class is a DAO with the only purpose of providing a mechanism to store
 * and retrieve all the required data for this app.
 */
@Suppress("TooManyFunctions")
interface ModelStorageDAO {

    /**
     * Insert a [Plant] using the provided parameters.
     */
    fun insertPlantEntry(plantId: Long?, scientificName: String, imageUrl: String)

    /**
     * Insert a list of [Plant].
     */
    fun insertPlantEntries(list: List<Plant>)

    /**
     * Insert a [PlantCommonName] using the provided parameters.
     */
    fun insertPlantCommonNameEntry(commonNameId: Long?, commonName: String, plantId: Long, locale: String)

    /**
     * Insert a list of [PlantCommonName].
     */
    fun insertPlantCommonNameEntries(list: List<PlantCommonName>)

    /**
     * Insert a [PlantMainName] using the provided parameters.
     */
    fun insertPlantMainNameEntry(mainNameId: Long?, mainName: String, plantId: Long, locale: String)

    /**
     * Insert a list of [PlantMainName].
     */
    fun insertPlantMainNameEntries(list: List<PlantMainName>)

    /**
     * Insert a [PlantFamily] using the provided parameters.
     */
    fun insertPlantFamilyNameEntry(familyId: Long?, family: String, plantId: Long, locale: String)

    /**
     * Insert a list of [PlantFamily].
     */
    fun insertPlantFamilyNameEntries(list: List<PlantFamily>)

    /**
     * Insert a [Toxicity] using the provided parameters.
     */
    fun insertToxicityEntry(toxicityId: Long?, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String)

    /**
     * Insert a list of [Toxicity].
     */
    fun insertToxicityEntries(list: List<Toxicity>)

    /**
     * Insert a [Toxicity] using the provided parameters.
     */
    fun insertDescriptionEntry(descriptionId: Long?, plantId: Long, animalType: AnimalType, description: String, locale: String)

    /**
     * Insert a list of [Description].
     */
    fun insertDescriptionEntries(list: List<Description>)

    /**
     * Get a [Plant] identified by the [scientificName].
     */
    fun getPlantEntry(scientificName: String): Plant?

    /**
     * Get a list all [Plant].
     */
    fun getAllPlantEntries(): List<Plant>

    /**
     * Get the number of [Plant] entries.
     */
    fun getPlantEntryCount(): Long

    /**
     * Get a list of all [PlantCommonName]
     */
    fun getAllPlantCommonNameEntries(): List<PlantCommonName>

    /**
     * Get a list of all [PlantMainName]
     */
    fun getAllPlantMainNameEntries(): List<PlantMainName>

    /**
     * Get a list of all [PlantFamily]
     */
    fun getAllPlantFamilyEntries(): List<PlantFamily>

    /**
     * Get a list of all [Toxicity]
     */
    fun getAllToxicityEntries(): List<Toxicity>

    /**
     * Get a list of all [Description]
     */
    fun getAllDescriptionEntries(): List<Description>

    /**
     * Get a list of all [PlantCommonName] that match the provided arguments.
     */
    fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName>

    /**
     * Get the [PlantMainName] that match the provided arguments.
     */
    fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName?

    /**
     * Get a [PlantFamily] that match the provided arguments.
     */
    fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily?

    /**
     * Get a [Toxicity] that match the provided arguments.
     */
    fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity?

    /**
     * Get a [Description] that match the provided arguments.
     */
    fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description?

    /**
     * Get a list of all [GetAllPlantsWithAnimalId] that match the provided arguments.
     */
    fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    /**
     * Get a [Flow] for the list of all [GetAllPlantsWithAnimalId] that match the provided arguments.
     */
    fun getCustomPlantEntriesFlow(animalType: AnimalType, locale: String): Flow<List<GetAllPlantsWithAnimalId>>

    /**
     * Get a [GetAllPlantsWithAnimalId] that match the provided arguments.
     */
    fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId?

    /**
     * Get a list of all [GetAllPlantsWithAnimalId] that match the provided arguments.
     */
    fun getCustomPlantEntriesPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<GetAllPlantsWithAnimalId>

    /**
     * Delete all stored entries.
     */
    fun deleteAll()
}
