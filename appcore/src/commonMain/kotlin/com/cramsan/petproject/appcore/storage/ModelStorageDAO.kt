package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.*

interface ModelStorageDAO {

    fun insertPlantEntry(scientificName: String, mainCommonName: String, family: String, imageUrl: String)

    fun insertPlantCommonNameEntry(commonName: String, plantId: Long, locale: String)

    fun insertPlantMainNameEntry(mainName: String, plantId: Long, locale: String)

    fun insertPlantFamilyNameEntry(family: String, plantId: Long, locale: String)

    fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalType: AnimalType, source:String)

    fun insertDescriptionEntry(plantId: Long, animalType: AnimalType, description:String, locale: String)

    fun getPlantEntry(scientificName: String): Plant

    fun getAllPlantEntries(): List<Plant>

    fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName>

    fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName

    fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily

    fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity

    fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description

    fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId

    fun deleteAll()
}