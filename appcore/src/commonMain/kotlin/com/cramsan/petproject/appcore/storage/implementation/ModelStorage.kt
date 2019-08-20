package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

class ModelStorage(
    initializer: ModelStorageInitializer,
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface
) : ModelStorageInterface {

    private var sqlDelightDAO: SQLDelightDAO = SQLDelightDAO(initializer)

    override fun insertPlant(plant: Plant) {
        eventLogger.log(Severity.INFO, classTag(), "insertPlant")
        threadUtil.assertIsBackgroundThread()

        sqlDelightDAO.insertPlantEntry(plant.id, plant.scientific_name, plant.image_url)
    }

    override fun getPlants(): List<Plant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlants")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllPlantEntries()
    }

    override fun insertPlantMainName(plantMainName: PlantMainName) {
        eventLogger.log(Severity.INFO, classTag(), "insertPlantMainName")
        threadUtil.assertIsBackgroundThread()

        sqlDelightDAO.insertPlantMainNameEntry(plantMainName.id, plantMainName.main_name, plantMainName.plant_id, plantMainName.locale)
    }

    override fun getPlantsMainName(): List<PlantMainName> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsMainName")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllPlantMainNameEntries()
    }

    override fun insertPlantCommonName(plantCommonName: PlantCommonName) {
        eventLogger.log(Severity.INFO, classTag(), "insertPlantCommonName")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.insertPlantCommonNameEntry(plantCommonName.id, plantCommonName.common_name, plantCommonName.plant_id, plantCommonName.locale)
    }

    override fun getPlantsCommonNames(): List<PlantCommonName> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsCommonNames")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllPlantCommonNameEntries()
    }

    override fun insertPlantFamily(plantFamily: PlantFamily) {
        eventLogger.log(Severity.INFO, classTag(), "insertPlantFamily")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.insertPlantFamilyNameEntry(plantFamily.id, plantFamily.family, plantFamily.plant_id, plantFamily.locale)
    }

    override fun getPlantsFamily(): List<PlantFamily> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsFamily")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllPlantFamilyEntries()
    }

    override fun insertDescription(description: Description) {
        eventLogger.log(Severity.INFO, classTag(), "insertDescription")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.insertDescriptionEntry(description.id, description.plant_id, description.animal_id, description.description, description.locale)
    }

    override fun getDescription(): List<Description> {
        eventLogger.log(Severity.INFO, classTag(), "getDescription")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllDescriptionEntries()
    }

    override fun insertToxicity(toxicity: Toxicity) {
        eventLogger.log(Severity.INFO, classTag(), "insertToxicity")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.insertToxicityEntry(toxicity.id, toxicity.is_toxic, toxicity.plant_id, toxicity.animal_id, toxicity.source)
    }

    override fun getToxicity(): List<Toxicity> {
        eventLogger.log(Severity.INFO, classTag(), "getToxicity")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getAllToxicityEntries()
    }

    override fun getCustomPlantEntry(
        animalType: AnimalType,
        plantId: Int,
        locale: String
    ): GetPlantWithPlantIdAndAnimalId? {
        eventLogger.log(Severity.INFO, classTag(), "getCustomPlantEntry")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getCustomPlantEntry(plantId.toLong(),
            animalType,
            locale)
    }

    override fun getCustomPlantsEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> {
        eventLogger.log(Severity.INFO, classTag(), "getCustomPlantsEntries")
        threadUtil.assertIsBackgroundThread()

        return sqlDelightDAO.getCustomPlantEntries(animalType, locale)
    }

    override fun deleteAll() {
        eventLogger.log(Severity.INFO, classTag(), "deleteAll")
        threadUtil.assertIsBackgroundThread()

        sqlDelightDAO.deleteAll()
    }
}
