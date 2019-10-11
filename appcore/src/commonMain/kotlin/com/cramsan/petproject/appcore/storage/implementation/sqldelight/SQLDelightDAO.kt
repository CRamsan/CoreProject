package com.cramsan.petproject.appcore.storage.implementation.sqldelight

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.db.SqlDriver

class SQLDelightDAO(sqlDriver: SqlDriver) : ModelStorageDAO {

    private var database: PetProjectDB = PetProjectDB(sqlDriver,
        DescriptionAdapter = com.cramsan.petproject.db.Description.Adapter(AnimalTypeAdapter()),
        ToxicityAdapter = com.cramsan.petproject.db.Toxicity.Adapter(AnimalTypeAdapter(), ToxicityValueAdapter())
    )

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
        val result = database.plantQueries.getPlant(scientificName).executeAsOneOrNull() ?: return null
        return Plant(result)
    }

    override fun getAllPlantEntries(): List<Plant> {
        val result = database.plantQueries.getAll().executeAsList()
        return result.map { Plant(it) }
    }

    override fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName> {
        val result = database.plantCommonNameQueries.getPlantCommonNames(plantId, locale).executeAsList()
        return result.map { PlantCommonName(it) }
    }

    override fun getAllPlantCommonNameEntries(): List<PlantCommonName> {
        val result = database.plantCommonNameQueries.getAllPlantCommonNames().executeAsList()
        return result.map { PlantCommonName(it) }
    }

    override fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName? {
        val result = database.plantMainNameQueries.getPlantMainName(plantId, locale).executeAsOneOrNull() ?: return null
        return PlantMainName(result)
    }

    override fun getAllPlantMainNameEntries(): List<PlantMainName> {
        val result = database.plantMainNameQueries.getAllPlantMainName().executeAsList()
        return result.map { PlantMainName(it) }
    }

    override fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily? {
        val result = database.plantFamilyQueries.getPlantFamily(plantId, locale).executeAsOneOrNull() ?: return null
        return PlantFamily(result)
    }

    override fun getAllPlantFamilyEntries(): List<PlantFamily> {
        val result = database.plantFamilyQueries.getAllPlantFamily().executeAsList()
        return result.map { PlantFamily(it) }
    }

    override fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity? {
        val result = database.toxicityQueries.getToxicity(plantId, animalType).executeAsOneOrNull() ?: return null
        return Toxicity(result)
    }

    override fun getAllToxicityEntries(): List<Toxicity> {
        val result = database.toxicityQueries.getAllToxicity().executeAsList()
        return result.map { Toxicity(it) }
    }

    override fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description? {
        val result = database.descriptionQueries.getDescription(plantId, animalType, locale).executeAsOneOrNull() ?: return null
        return Description(result)
    }

    override fun getAllDescriptionEntries(): List<Description> {
        val result = database.descriptionQueries.getAllDescription().executeAsList()
        return result.map { Description(it) }
    }

    override fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> {
        val result = database.customProjectionsQueries.getAllPlantsWithAnimalId(animalType, locale).executeAsList()
        return result.map { GetAllPlantsWithAnimalId(it) }
    }

    override fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId? {
        val result = database.customProjectionsQueries.getPlantWithPlantIdAndAnimalId(animalType, plantId, locale).executeAsOneOrNull() ?: return null
        return GetPlantWithPlantIdAndAnimalId(result)
    }

    override fun deleteAll() {
        database.toxicityQueries.deleteAll()
        database.plantCommonNameQueries.deleteAll()
        database.plantFamilyQueries.deleteAll()
        database.descriptionQueries.deleteAll()
        database.plantQueries.deleteAll()
    }
}
