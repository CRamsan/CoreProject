package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.*

interface ModelStorageDAO {

    fun insertAnimalEntry(animalType: AnimalType)

    fun insertPlantEntry(scientificName: String, mainCommonName: String, family: String, imageUrl: String)

    fun insertPlantCommonNameEntry(commonName: String, plantId: Long)

    fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalId: Long, source:String)

    fun insertDescriptionEntry(plantId: Long, animalId: Long, description:String)

    fun getAnimalEntry(animalType: AnimalType): Animal

    fun getPlantEntry(scientificName: String): Plant

    fun getAllPlantEntries(): List<Plant>

    fun getPlantCommonNameEntries(plantId: Long): List<PlantCommonName>

    fun getToxicityEntry(plantId: Long, animalId: Long): Toxicity

    fun getDescriptionEntry(plantId: Long, animalId: Long): Description

    fun getCustomPlantEntries(): List<GetAllPlants>

    fun getCustomPlantEntries(animalId: Long): List<GetAllPlantsWithAnimalId>

    fun deleteAll()
}