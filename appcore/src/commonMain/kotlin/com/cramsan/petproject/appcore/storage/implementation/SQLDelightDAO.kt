package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.db.*

class SQLDelightDAO(initializer: ModelStorageInitializer) : ModelStorageDAO {

    private var database: PetProjectDB

    init {
        val sqlDriver = initializer.platformInitializer.getSqlDriver()
        database = PetProjectDB(sqlDriver,
            AnimalAdapter = Animal.Adapter(CommonNameAdapter())
        )
    }

    override fun insertAnimalEntry(animalType: AnimalType) {
        database.animalQueries.insert(animalType)
    }

    override fun insertPlantEntry(scientificName: String, mainCommonName: String, family: String, imageUrl: String) {
        database.plantQueries.insert(scientificName, mainCommonName, family, imageUrl)
    }

    override fun insertPlantCommonNameEntry(commonName: String, plantId: Long) {
        database.plantCommonNameQueries.insert(commonName, plantId)
    }

    override fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalId: Long, source:String) {
        database.toxicityQueries.insert(plantId, animalId, isToxic, source)
    }

    override fun insertDescriptionEntry(plantId: Long, animalId: Long, description: String) {
        return database.descriptionQueries.insert(plantId, animalId, description)
    }

    override fun getAnimalEntry(animalType: AnimalType): Animal {
        return database.animalQueries.getAnimal(animalType).executeAsOne()
    }

    override fun getPlantEntry(scientificName: String): com.cramsan.petproject.db.Plant {
        return database.plantQueries.getPlant(scientificName).executeAsOne()
    }

    override fun getAllPlantEntries(): List<Plant> {
        return database.plantQueries.getAll().executeAsList()
    }

    override fun getPlantCommonNameEntries(plantId: Long): List<PlantCommonName> {
        return database.plantCommonNameQueries.getPlantCommonNames(plantId).executeAsList()
    }

    override fun getToxicityEntry(plantId: Long, animalId: Long): Toxicity {
        return database.toxicityQueries.getToxicity(plantId, animalId).executeAsOne()
    }

    override fun getDescriptionEntry(plantId: Long, animalId: Long): Description {
        return database.descriptionQueries.getDescription(plantId, animalId).executeAsOne()
    }

    override fun getCustomPlantEntries(): List<GetAllPlants> {
        return database.customProjectionsQueries.getAllPlants().executeAsList()
    }

    override fun getCustomPlantEntries(animalId: Long): List<GetAllPlantsWithAnimalId> {
        return database.customProjectionsQueries.getAllPlantsWithAnimalId(animalId).executeAsList()
    }

    override fun deleteAll() {
        database.toxicityQueries.deleteAll()
        database.plantCommonNameQueries.deleteAll()
        database.plantQueries.deleteAll()
        database.animalQueries.deleteAll()
    }
}