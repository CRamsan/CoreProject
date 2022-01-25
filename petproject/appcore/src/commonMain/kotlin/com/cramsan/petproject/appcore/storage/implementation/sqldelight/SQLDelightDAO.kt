package com.cramsan.petproject.appcore.storage.implementation.sqldelight

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [ModelStorageDAO] that uses sqldelight.
 */
@Suppress("TooManyFunctions")
class SQLDelightDAO(sqlDriver: SqlDriver) : ModelStorageDAO {

    private var database: PetProjectDB = PetProjectDB(
        sqlDriver,
        DescriptionAdapter = com.cramsan.petproject.db.Description.Adapter(AnimalTypeAdapter()),
        ToxicityAdapter = com.cramsan.petproject.db.Toxicity.Adapter(AnimalTypeAdapter(), ToxicityValueAdapter())
    )

    override fun insertPlantEntry(plantId: Long?, scientificName: String, imageUrl: String) {
        if (plantId != null) {
            database.plantQueries.insert(plantId, scientificName, imageUrl)
        } else {
            database.plantQueries.insertNew(scientificName, imageUrl)
        }
    }

    override fun insertPlantEntries(list: List<com.cramsan.petproject.appcore.storage.Plant>) {
        database.plantQueries.transaction {
            list.forEach { insertPlantEntry(it.id, it.scientificName, it.imageUrl) }
        }
    }

    override fun insertPlantCommonNameEntry(commonNameId: Long?, commonName: String, plantId: Long, locale: String) {
        if (commonNameId != null) {
            database.plantCommonNameQueries.insert(commonNameId, commonName, plantId, locale)
        } else {
            database.plantCommonNameQueries.insertNew(commonName, plantId, locale)
        }
    }

    override fun insertPlantCommonNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantCommonName>) {
        database.plantCommonNameQueries.transaction {
            list.forEach { insertPlantCommonNameEntry(it.id, it.commonName, it.plantId, it.locale) }
        }
    }

    override fun insertPlantMainNameEntry(mainNameId: Long?, mainName: String, plantId: Long, locale: String) {
        if (mainNameId != null) {
            database.plantMainNameQueries.insert(mainNameId, plantId, mainName, locale)
        } else {
            database.plantMainNameQueries.insertNew(plantId, mainName, locale)
        }
    }

    override fun insertPlantMainNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantMainName>) {
        database.plantMainNameQueries.transaction {
            list.forEach { insertPlantMainNameEntry(it.id, it.mainName, it.plantId, it.locale) }
        }
    }

    override fun insertPlantFamilyNameEntry(familyId: Long?, family: String, plantId: Long, locale: String) {
        if (familyId != null) {
            database.plantFamilyQueries.insert(familyId, plantId, family, locale)
        } else {
            database.plantFamilyQueries.insertNew(plantId, family, locale)
        }
    }

    override fun insertPlantFamilyNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantFamily>) {
        database.plantFamilyQueries.transaction {
            list.forEach { insertPlantFamilyNameEntry(it.id, it.family, it.plantId, it.locale) }
        }
    }

    override fun insertToxicityEntry(toxicityId: Long?, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String) {
        if (toxicityId != null) {
            database.toxicityQueries.insert(toxicityId, plantId, animalType, isToxic, source)
        } else {
            database.toxicityQueries.insertNew(plantId, animalType, isToxic, source)
        }
    }

    override fun insertToxicityEntries(list: List<com.cramsan.petproject.appcore.storage.Toxicity>) {
        database.toxicityQueries.transaction {
            list.forEach { insertToxicityEntry(it.id, it.toxic, it.plantId, it.animalId, it.source) }
        }
    }

    override fun insertDescriptionEntry(descriptionId: Long?, plantId: Long, animalType: AnimalType, description: String, locale: String) {
        return database.descriptionQueries.insert(descriptionId, plantId, animalType, description, locale)
    }

    override fun insertDescriptionEntries(list: List<com.cramsan.petproject.appcore.storage.Description>) {
        database.descriptionQueries.transaction {
            list.forEach { insertDescriptionEntry(it.id, it.plantId, it.animalId, it.description, it.locale) }
        }
    }

    override fun getPlantEntry(scientificName: String): Plant? {
        val result = database.plantQueries.getPlant(scientificName).executeAsOneOrNull() ?: return null
        return Plant(result)
    }

    override fun getAllPlantEntries(): List<Plant> {
        val result = database.plantQueries.getAll().executeAsList()
        return result.map { Plant(it) }
    }

    override fun getPlantEntryCount(): Long {
        return database.customProjectionsQueries.countPlants().executeAsOne()
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
        return getCustomPlantEntriesPaginated(animalType, locale, Long.MAX_VALUE, 0)
    }

    override fun getCustomPlantEntriesFlow(
        animalType: AnimalType,
        locale: String
    ): Flow<List<GetAllPlantsWithAnimalId>> {
        return if (animalType == AnimalType.ALL) {
            database.customProjectionsQueries.getAllPlantsWithAnimalIdAll(locale, Long.MAX_VALUE, 0).asFlow().mapToList().map { list ->
                list.map { GetAllPlantsWithAnimalId(it) }
            }
        } else {
            database.customProjectionsQueries.getAllPlantsWithAnimalId(animalType, locale, Long.MAX_VALUE, 0).asFlow().mapToList().map { list ->
                list.map { GetAllPlantsWithAnimalId(it) }
            }
        }
    }

    override fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId? {
        val result = database.customProjectionsQueries.getPlantWithPlantIdAndAnimalId(animalType, plantId, locale).executeAsOneOrNull() ?: return null
        return GetPlantWithPlantIdAndAnimalId(result)
    }

    override fun getCustomPlantEntriesPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<GetAllPlantsWithAnimalId> {
        return if (animalType == AnimalType.ALL) {
            val result = database.customProjectionsQueries.getAllPlantsWithAnimalIdAll(locale, limit, offset).executeAsList()
            result.map { GetAllPlantsWithAnimalId(it) }
        } else {
            val result = database.customProjectionsQueries.getAllPlantsWithAnimalId(animalType, locale, limit, offset).executeAsList()
            result.map { GetAllPlantsWithAnimalId(it) }
        }
    }

    override fun deleteAll() {
        database.toxicityQueries.deleteAll()
        database.plantCommonNameQueries.deleteAll()
        database.plantFamilyQueries.deleteAll()
        database.descriptionQueries.deleteAll()
        database.plantQueries.deleteAll()
    }
}
