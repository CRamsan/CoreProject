package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

interface ModelProviderDAO {

    fun getPlantEntry(scientificName: String): Plant

    fun getAllPlantEntries(): List<Plant>

    fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName>

    fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName

    fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily

    fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity

    fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description

    fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId
}
