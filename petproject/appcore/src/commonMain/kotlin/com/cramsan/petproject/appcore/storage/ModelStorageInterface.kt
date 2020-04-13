package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType

interface ModelStorageInterface {

    fun insertPlant(plant: Plant)

    fun insertPlantList(list: List<Plant>)

    fun getPlant(scientificName: String): Plant?

    fun getPlants(): List<Plant>

    fun getPlantCount(): Long

    fun insertPlantMainName(plantMainName: PlantMainName)

    fun insertPlantMainNameList(list: List<PlantMainName>)

    fun getPlantMainName(plantId: Long, locale: String): PlantMainName?

    fun getPlantsMainName(): List<PlantMainName>

    fun insertPlantCommonName(plantCommonName: PlantCommonName)

    fun insertPlantCommonNameList(list: List<PlantCommonName>)

    fun getPlantsCommonNames(): List<PlantCommonName>

    fun insertPlantFamily(plantFamily: PlantFamily)

    fun insertPlantFamilyList(list: List<PlantFamily>)

    fun getPlantFamily(plantId: Long, locale: String): PlantFamily?

    fun getPlantsFamily(): List<PlantFamily>

    fun insertDescription(description: Description)

    fun insertDescriptionList(list: List<Description>)

    fun getDescription(): List<Description>

    fun insertToxicity(toxicity: Toxicity)

    fun insertToxicityList(list: List<Toxicity>)

    fun getToxicity(plantId: Long, animalType: AnimalType): Toxicity?

    fun getToxicity(): List<Toxicity>

    fun getCustomPlantEntry(animalType: AnimalType, plantId: Int, locale: String): GetPlantWithPlantIdAndAnimalId?

    fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun getCustomPlantEntriesPaginated(animalType: AnimalType,
                                       locale: String,
                                       limit: Long,
                                       offset: Long): List<GetAllPlantsWithAnimalId>

    fun deleteAll()
}
