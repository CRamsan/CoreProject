package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.*

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