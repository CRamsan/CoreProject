package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.db.*

class SQLDelightDAO(initializer: ModelStorageInitializer) : ModelStorageDAO {

    private var database: PetProjectDB

    init {
        val sqlDriver = initializer.platformInitializer.getSqlDriver()
        database = PetProjectDB(sqlDriver,
            DescriptionAdapter = Description.Adapter(AnimalTypeAdapter()),
            ToxicityAdapter = Toxicity.Adapter(AnimalTypeAdapter())
        )
    }

    override fun insertPlantEntry(scientificName: String, mainCommonName: String, family: String, imageUrl: String) {
        database.plantQueries.insert(scientificName, mainCommonName, family, imageUrl)
    }

    override fun insertPlantCommonNameEntry(commonName: String, plantId: Long) {
        database.plantCommonNameQueries.insert(commonName, plantId)
    }

    override fun insertToxicityEntry(isToxic: Boolean, plantId: Long, animalType: AnimalType, source:String) {
        database.toxicityQueries.insert(plantId, animalType, isToxic, source)
    }

    override fun insertDescriptionEntry(plantId: Long, animalType: AnimalType, description: String) {
        return database.descriptionQueries.insert(plantId, animalType, description)
    }

    override fun getPlantEntry(scientificName: String): Plant {
        return database.plantQueries.getPlant(scientificName).executeAsOne()
    }

    override fun getAllPlantEntries(): List<Plant> {
        return database.plantQueries.getAll().executeAsList()
    }

    override fun getPlantCommonNameEntries(plantId: Long): List<PlantCommonName> {
        return database.plantCommonNameQueries.getPlantCommonNames(plantId).executeAsList()
    }

    override fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity {
        return database.toxicityQueries.getToxicity(plantId, animalType).executeAsOne()
    }

    override fun getDescriptionEntry(plantId: Long, animalType: AnimalType): Description {
        return database.descriptionQueries.getDescription(plantId, animalType).executeAsOne()
    }

    override fun getCustomPlantEntries(animalType: AnimalType): List<GetAllPlantsWithAnimalId> {
        return database.customProjectionsQueries.getAllPlantsWithAnimalId(animalType).executeAsList()
    }

    override fun getCustomPlantEntry(plantId: Long, animalType: AnimalType): GetPlantWithPlantIdAndAnimalId {
        return database.customProjectionsQueries.getPlantWithPlantIdAndAnimalId(animalType, plantId).executeAsOne()
    }

    override fun deleteAll() {
        database.toxicityQueries.deleteAll()
        database.plantCommonNameQueries.deleteAll()
        database.plantQueries.deleteAll()
    }
}