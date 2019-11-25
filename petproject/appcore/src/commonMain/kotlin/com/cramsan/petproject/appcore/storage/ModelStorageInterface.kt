package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType

interface ModelStorageInterface {

    fun insertPlant(plant: Plant)

    fun getPlant(scientificName: String): Plant?

    fun getPlants(): List<Plant>

    fun insertPlantMainName(plantMainName: PlantMainName)

    fun getPlantMainName(plantId: Long, locale: String): PlantMainName?

    fun getPlantsMainName(): List<PlantMainName>

    fun insertPlantCommonName(plantCommonName: PlantCommonName)

    fun getPlantsCommonNames(): List<PlantCommonName>

    fun insertPlantFamily(plantFamily: PlantFamily)

    fun getPlantFamily(plantId: Long, locale: String): PlantFamily?

    fun getPlantsFamily(): List<PlantFamily>

    fun insertDescription(description: Description)

    fun getDescription(): List<Description>

    fun insertToxicity(toxicity: Toxicity)

    fun getToxicity(plantId: Long, animalType: AnimalType): Toxicity?

    fun getToxicity(): List<Toxicity>

    fun getCustomPlantEntry(animalType: AnimalType, plantId: Int, locale: String): GetPlantWithPlantIdAndAnimalId?

    fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun deleteAll()
}
