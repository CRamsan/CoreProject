package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

interface ModelStorageDAO {

    fun insertPlantEntry(plantId: Long, scientificName: String, imageUrl: String)

    fun insertPlantCommonNameEntry(commonNameId: Long, commonName: String, plantId: Long, locale: String)

    fun insertPlantMainNameEntry(mainNameId: Long, mainName: String, plantId: Long, locale: String)

    fun insertPlantFamilyNameEntry(familyId: Long, family: String, plantId: Long, locale: String)

    fun insertToxicityEntry(toxicityId: Long, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String)

    fun insertDescriptionEntry(descriptionId: Long, plantId: Long, animalType: AnimalType, description: String, locale: String)

    fun getPlantEntry(scientificName: String): Plant?

    fun getAllPlantEntries(): List<Plant>

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

    fun deleteAll()
}
