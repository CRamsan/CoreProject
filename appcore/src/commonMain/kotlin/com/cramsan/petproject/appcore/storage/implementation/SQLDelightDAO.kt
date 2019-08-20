package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.PetProjectDB
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

class SQLDelightDAO(initializer: ModelStorageInitializer) : ModelStorageDAO {

    private var database: PetProjectDB

    init {
        val sqlDriver = initializer.platformInitializer.getSqlDriver()

        database = PetProjectDB(sqlDriver,
            DescriptionAdapter = Description.Adapter(AnimalTypeAdapter()),
            ToxicityAdapter = Toxicity.Adapter(AnimalTypeAdapter(), ToxicityValueAdapter())
        )
        initializer.platformInitializer.afterConnecting(sqlDriver)
    }

    override fun insertPlantEntry(plantId: Long, scientificName: String, imageUrl: String) {
        database.plantQueries.insert(plantId, scientificName, imageUrl)
    }

    override fun insertPlantCommonNameEntry(commonNameId: Long, commonName: String, plantId: Long, locale: String) {
        database.plantCommonNameQueries.insert(commonNameId, commonName, plantId, locale)
    }

    override fun insertPlantMainNameEntry(mainNameId: Long, mainName: String, plantId: Long, locale: String) {
        database.plantMainNameQueries.insert(mainNameId, plantId, mainName, locale)
    }

    override fun insertPlantFamilyNameEntry(familyId: Long, family: String, plantId: Long, locale: String) {
        database.plantFamilyQueries.insert(familyId, plantId, family, locale)
    }

    override fun insertToxicityEntry(toxicityId: Long, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String) {
        database.toxicityQueries.insert(toxicityId, plantId, animalType, isToxic, source)
    }

    override fun insertDescriptionEntry(descriptionId: Long, plantId: Long, animalType: AnimalType, description: String, locale: String) {
        return database.descriptionQueries.insert(descriptionId, plantId, animalType, description, locale)
    }

    override fun getPlantEntry(scientificName: String): Plant? {
        return database.plantQueries.getPlant(scientificName).executeAsOneOrNull()
    }

    override fun getAllPlantEntries(): List<Plant> {
        return database.plantQueries.getAll().executeAsList()
    }

    override fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName> {
        return database.plantCommonNameQueries.getPlantCommonNames(plantId, locale).executeAsList()
    }

    override fun getAllPlantCommonNameEntries(): List<PlantCommonName> {
        return database.plantCommonNameQueries.getAllPlantCommonNames().executeAsList()
    }

    override fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName? {
        return database.plantMainNameQueries.getPlantMainName(plantId, locale).executeAsOneOrNull()
    }

    override fun getAllPlantMainNameEntries(): List<PlantMainName> {
        return database.plantMainNameQueries.getAllPlantMainName().executeAsList()
    }

    override fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily? {
        return database.plantFamilyQueries.getPlantFamily(plantId, locale).executeAsOneOrNull()
    }

    override fun getAllPlantFamilyEntries(): List<PlantFamily> {
        return database.plantFamilyQueries.getAllPlantFamily().executeAsList()
    }

    override fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity? {
        return database.toxicityQueries.getToxicity(plantId, animalType).executeAsOneOrNull()
    }

    override fun getAllToxicityEntries(): List<Toxicity> {
        return database.toxicityQueries.getAllToxicity().executeAsList()
    }

    override fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description? {
        return database.descriptionQueries.getDescription(plantId, animalType, locale).executeAsOneOrNull()
    }

    override fun getAllDescriptionEntries(): List<Description> {
        return database.descriptionQueries.getAllDescription().executeAsList()
    }

    override fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> {
        return database.customProjectionsQueries.getAllPlantsWithAnimalId(animalType, locale).executeAsList()
    }

    override fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId? {
        return database.customProjectionsQueries.getPlantWithPlantIdAndAnimalId(animalType, plantId, locale).executeAsOneOrNull()
    }

    override fun deleteAll() {
        database.toxicityQueries.deleteAll()
        database.plantCommonNameQueries.deleteAll()
        database.plantQueries.deleteAll()
    }
}
