package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

interface ModelStorageInterface {

    fun insertPlant(plant: Plant)

    fun getPlants(): List<Plant>

    fun insertPlantMainName(plantMainName: PlantMainName)

    fun getPlantsMainName(): List<PlantMainName>

    fun insertPlantCommonName(plantCommonName: PlantCommonName)

    fun getPlantsCommonNames(): List<PlantCommonName>

    fun insertPlantFamily(plantFamily: PlantFamily)

    fun getPlantsFamily(): List<PlantFamily>

    fun insertDescription(description: Description)

    fun getDescription(): List<Description>

    fun insertToxicity(toxicity: Toxicity)

    fun getToxicity(): List<Toxicity>

    fun getCustomPlantEntry(animalType: AnimalType, plantId: Int, locale: String): GetPlantWithPlantIdAndAnimalId?

    fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId>

    fun deleteAll()
}
