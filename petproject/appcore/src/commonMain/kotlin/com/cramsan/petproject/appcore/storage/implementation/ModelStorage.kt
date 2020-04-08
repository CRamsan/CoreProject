package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.GetAllPlantsWithAnimalId
import com.cramsan.petproject.appcore.storage.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity

class ModelStorage(
    private val platformDelegate: ModelStorageDAO,
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface
) : ModelStorageInterface {

    override fun insertPlant(plant: Plant) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertPlant")
        threadUtil.assertIsBackgroundThread()

        val plantId = if (plant.id.toInt() == -1) {
            null
        } else {
            plant.id
        }
        platformDelegate.insertPlantEntry(plantId, plant.scientificName, plant.imageUrl)
    }

    override fun getPlants(): List<Plant> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlants")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllPlantEntries()
    }

    override fun getPlant(scientificName: String): Plant? {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlant")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getPlantEntry(scientificName)
    }

    override fun insertPlantMainName(plantMainName: PlantMainName) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertPlantMainName")
        threadUtil.assertIsBackgroundThread()

        val plantMainNameId = if (plantMainName.id.toInt() == -1) {
            null
        } else {
            plantMainName.id
        }
        platformDelegate.insertPlantMainNameEntry(plantMainNameId, plantMainName.mainName, plantMainName.plantId, plantMainName.locale)
    }

    override fun getPlantMainName(plantId: Long, locale: String): PlantMainName? {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlantMainName")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getPlantMainNameEntry(plantId, locale)
    }

    override fun getPlantsMainName(): List<PlantMainName> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlantsMainName")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllPlantMainNameEntries()
    }

    override fun insertPlantCommonName(plantCommonName: PlantCommonName) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertPlantCommonName")
        threadUtil.assertIsBackgroundThread()

        val plantCommonNameId = if (plantCommonName.id.toInt() == -1) {
            null
        } else {
            plantCommonName.id
        }
        return platformDelegate.insertPlantCommonNameEntry(plantCommonNameId, plantCommonName.commonName, plantCommonName.plantId, plantCommonName.locale)
    }

    override fun getPlantsCommonNames(): List<PlantCommonName> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlantsCommonNames")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllPlantCommonNameEntries()
    }

    override fun insertPlantFamily(plantFamily: PlantFamily) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertPlantFamily")
        threadUtil.assertIsBackgroundThread()

        val plantFamilyId = if (plantFamily.id.toInt() == -1) {
            null
        } else {
            plantFamily.id
        }
        return platformDelegate.insertPlantFamilyNameEntry(plantFamilyId, plantFamily.family, plantFamily.plantId, plantFamily.locale)
    }

    override fun getPlantFamily(plantId: Long, locale: String): PlantFamily? {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlantFamily")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getPlantFamilyEntry(plantId, locale)
    }

    override fun getPlantsFamily(): List<PlantFamily> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getPlantsFamily")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllPlantFamilyEntries()
    }

    override fun insertDescription(description: Description) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertDescription")
        threadUtil.assertIsBackgroundThread()

        val descriptionId = if (description.id.toInt() == -1) {
            null
        } else {
            description.id
        }
        return platformDelegate.insertDescriptionEntry(descriptionId, description.plantId, description.animalId, description.description, description.locale)
    }

    override fun getDescription(): List<Description> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getDescription")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllDescriptionEntries()
    }

    override fun insertToxicity(toxicity: Toxicity) {
        eventLogger.log(Severity.INFO, "ModelStorage", "insertToxicity")
        threadUtil.assertIsBackgroundThread()

        val toxicityId = if (toxicity.id.toInt() == -1) {
            null
        } else {
            toxicity.id
        }
        return platformDelegate.insertToxicityEntry(toxicityId, toxicity.isToxic, toxicity.plantId, toxicity.animalId, toxicity.source)
    }

    override fun getToxicity(plantId: Long, animalType: AnimalType): Toxicity? {
        eventLogger.log(Severity.INFO, "ModelStorage", "getToxicity")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getToxicityEntry(plantId, animalType)
    }

    override fun getToxicity(): List<Toxicity> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getToxicity")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getAllToxicityEntries()
    }

    override fun getCustomPlantEntry(
        animalType: AnimalType,
        plantId: Int,
        locale: String
    ): GetPlantWithPlantIdAndAnimalId? {
        eventLogger.log(Severity.INFO, "ModelStorage", "getCustomPlantEntry")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getCustomPlantEntry(plantId.toLong(),
            animalType,
            locale)
    }

    override fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> {
        eventLogger.log(Severity.INFO, "ModelStorage", "getCustomPlantsEntries")
        threadUtil.assertIsBackgroundThread()

        return platformDelegate.getCustomPlantEntries(animalType, locale)
    }

    override fun deleteAll() {
        eventLogger.log(Severity.INFO, "ModelStorage", "deleteAll")
        threadUtil.assertIsBackgroundThread()

        platformDelegate.deleteAll()
    }
}
