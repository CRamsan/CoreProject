package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.*

interface ModelStorageDAO {

    fun insertAnimalEntry(animalType: AnimalType)

    fun insertPlantEntry(scientificName: String, family: String, imageUrl: String)

    fun insertPlantCommonNameEntry(commonName: String, plantId: Long)

    fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalId: Long, source:String)

    fun getAnimalEntry(animalType: AnimalType): Animal

    fun getPlantEntry(scientificName: String): Plant

    fun getAllPlantEntries(): List<Plant>

    fun getPlantCommonNameEntries(plantId: Long): List<PlantCommonName>

    fun getToxicityEntry(plantId: Long, animalId: Long): Toxicity

    fun getCustomPlantEntries(): List<GetAllPlants>

    fun deleteAll()
}