package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue

interface ModelStorageDAO {

    fun insertPlantEntry(plantId: Long?, scientificName: String, imageUrl: String)

    fun insertPlantEntries(list: List<Plant>)

    fun insertPlantCommonNameEntry(commonNameId: Long?, commonName: String, plantId: Long, locale: String)

    fun insertPlantCommonNameEntries(list: List<PlantCommonName>)

    fun insertPlantMainNameEntry(mainNameId: Long?, mainName: String, plantId: Long, locale: String)

    fun insertPlantMainNameEntries(list: List<PlantMainName>)

    fun insertPlantFamilyNameEntry(familyId: Long?, family: String, plantId: Long, locale: String)

    fun insertPlantFamilyNameEntries(list: List<PlantFamily>)

    fun insertToxicityEntry(toxicityId: Long?, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String)

    fun insertToxicityEntries(list: List<Toxicity>)

    fun insertDescriptionEntry(descriptionId: Long?, plantId: Long, animalType: AnimalType, description: String, locale: String)

    fun insertDescriptionEntries(list: List<Description>)

    fun getPlantEntry(scientificName: String): Plant?

    fun getAllPlantEntries(): List<Plant>

    fun getPlantEntryCount(): Long

    fun getAllPlantCommonNameEntries(): List<PlantCommonName>

    fun getAllPlantMainNameEntries(): List<PlantMainName>

    fun getAllPlantFamilyEntries(): List<PlantFamily>

    fun getAllToxicityEntries(): List<Toxicity>

    fun getAllDescriptionEntries(): List<Description>

    fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName>

    fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName?

    fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily?

    fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity?

    fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description?

    fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId?

    fun getCustomPlantEntriesPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<GetAllPlantsWithAnimalId>
    fun deleteAll()
}
