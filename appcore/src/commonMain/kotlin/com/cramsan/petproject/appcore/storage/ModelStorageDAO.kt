package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.*

interface ModelStorageDAO {

    fun insertPlantEntry(scientificName: String, mainCommonName: String, family: String, imageUrl: String)

    fun insertPlantCommonNameEntry(commonName: String, plantId: Long)

    fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalType: AnimalType, source:String)

    fun insertDescriptionEntry(plantId: Long, animalType: AnimalType, description:String)

    fun getPlantEntry(scientificName: String): Plant

    fun getAllPlantEntries(): List<Plant>

    fun getPlantCommonNameEntries(plantId: Long): List<PlantCommonName>

    fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity

    fun getDescriptionEntry(plantId: Long, animalType: AnimalType): Description

    fun getCustomPlantEntries(animalType: AnimalType): List<GetAllPlantsWithAnimalId>

    fun getCustomPlantEntry(plantId: Long, animalType: AnimalType): GetPlantWithPlantIdAndAnimalId

    fun deleteAll()
}