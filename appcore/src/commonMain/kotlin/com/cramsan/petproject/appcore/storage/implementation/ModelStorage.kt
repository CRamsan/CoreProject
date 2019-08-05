package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.storage.ModelStorageInterface

class ModelStorage(
    initializer: ModelStorageInitializer,
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface
) : ModelStorageInterface {

    private var sqlDelightDAO: SQLDelightDAO = SQLDelightDAO(initializer)

    override fun getPlants(animalType: AnimalType, locale: String): List<Plant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlants")
        threadUtil.assertIsBackgroundThread()

        val list = sqlDelightDAO.getCustomPlantEntries(animalType, locale)
        val mutableList = mutableListOf<Plant>()

        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientific_name,
                    it.main_name,
                    it.common_names,
                    it.image_url,
                    it.family
                ))
        }
        return mutableList
    }

    override fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsWithToxicity")
        threadUtil.assertIsBackgroundThread()

        val list = sqlDelightDAO.getCustomPlantEntries(animalType, locale)
        val mutableList = mutableListOf<PresentablePlant>()

        list.forEach {
            mutableList.add(
                PresentablePlant(
                    it.id,
                    it.scientific_name,
                    it.main_name,
                    it.is_toxic
                ))
        }
        return mutableList
    }

    override fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant? {
        eventLogger.log(Severity.INFO, classTag(), "getPlant")
        threadUtil.assertIsBackgroundThread()

        val plantEntry = sqlDelightDAO.getCustomPlantEntry(plantId.toLong(), animalType, locale) ?: return null

        return Plant(
            plantEntry.id.toInt(),
            plantEntry.scientific_name,
            plantEntry.main_name,
            plantEntry.common_names,
            plantEntry.image_url,
            plantEntry.family
        )
    }

    override fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata? {
        eventLogger.log(Severity.INFO, classTag(), "getPlantMetadata")
        threadUtil.assertIsBackgroundThread()

        val plantCustomEntry = sqlDelightDAO.getCustomPlantEntry(plantId.toLong(), animalType, locale) ?: return null
        return PlantMetadata(plantId, animalType, plantCustomEntry.is_toxic, plantCustomEntry.description, plantCustomEntry.source)
    }

    override fun insertPlant(plant: Plant, plantMetadata: PlantMetadata, locale: String) {
        eventLogger.log(Severity.INFO, classTag(), "insertPlant")
        threadUtil.assertIsBackgroundThread()

        var isNew = false
        if (sqlDelightDAO.getPlantEntry(plant.exactName) == null) {
            sqlDelightDAO.insertPlantEntry(plant.exactName, plant.mainCommonName, plant.family, plant.imageUrl)
            isNew = true
        }

        val plantEntry = sqlDelightDAO.getPlantEntry(plant.exactName)
        if (plantEntry == null) {
            eventLogger.log(Severity.ERROR, classTag(), "entry is null after writing")
            return
        }

        if (isNew) {
            sqlDelightDAO.insertPlantFamilyNameEntry(plant.family, plantEntry.id, locale)
            sqlDelightDAO.insertPlantMainNameEntry(plant.mainCommonName, plantEntry.id, locale)
        }

        if (sqlDelightDAO.getDescriptionEntry(plantEntry.id, plantMetadata.animalType, locale) == null) {
            sqlDelightDAO.insertDescriptionEntry(
                plantEntry.id,
                plantMetadata.animalType,
                plantMetadata.description,
                locale
            )
        }

        if (sqlDelightDAO.getToxicityEntry(plantEntry.id, plantMetadata.animalType) == null) {
            sqlDelightDAO.insertToxicityEntry(
                plantMetadata.isToxic,
                plantEntry.id,
                plantMetadata.animalType,
                plantMetadata.source
            )
        }

        val names = plant.commonNames.split(", ")
        for (name in names) {
            if (name.trim().isEmpty())
                continue

            val nameString = name.trim()
            val commonNamesResult = sqlDelightDAO.getPlantCommonNameEntries(plantEntry.id, locale)
            var shouldSkip = false
            for (plantName in commonNamesResult) {
                if (plantName.common_name.trim().equals(nameString)) {
                    shouldSkip = true
                    break
                }
            }
            if (shouldSkip) {
                continue
            }
            sqlDelightDAO.insertPlantCommonNameEntry(name, plantEntry.id, locale)
        }
    }

    override fun deleteAll() {
        eventLogger.log(Severity.INFO, classTag(), "deleteAll")
        threadUtil.assertIsBackgroundThread()

        sqlDelightDAO.deleteAll()
    }
}
