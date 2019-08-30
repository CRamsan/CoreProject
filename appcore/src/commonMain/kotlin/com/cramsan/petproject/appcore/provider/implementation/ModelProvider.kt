package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModelProvider(
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface,
    private val modelStorage: ModelStorageInterface
) : ModelProviderInterface {

    lateinit var plantList: List<PresentablePlant>
    var filterJob: Job? = null

    override fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant? {
        eventLogger.log(Severity.INFO, classTag(), "getPlant")
        threadUtil.assertIsBackgroundThread()

        val plantEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null

        return Plant(
            plantEntry.id.toInt(),
            plantEntry.scientific_name,
            plantEntry.main_name,
            plantEntry.common_names,
            plantEntry.image_url,
            plantEntry.family
        )
    }

    override fun getPlants(animalType: AnimalType, locale: String): List<Plant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlants")
        threadUtil.assertIsBackgroundThread()

        val list = modelStorage.getCustomPlantsEntries(animalType, locale)
        val mutableList = mutableListOf<Plant>()

        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientific_name,
                    it.main_name,
                    "",
                    it.image_url,
                    it.family
                ))
        }
        return mutableList
    }

    override fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsWithToxicity")
        threadUtil.assertIsBackgroundThread()

        val list = modelStorage.getCustomPlantsEntries(animalType, locale)
        val mutableList = mutableListOf<PresentablePlant>()

        list.forEach {
            mutableList.add(
                PresentablePlant(
                    it.id,
                    it.scientific_name,
                    it.main_name,
                    it.is_toxic ?: ToxicityValue.UNDETERMINED
                ))
        }
        plantList = mutableList.sortedBy { it.mainCommonName }
        return plantList
    }

    override suspend fun getPlantsWithToxicityFiltered(
        animalType: AnimalType,
        query: String,
        locale: String
    ): List<PresentablePlant>? = withContext(Dispatchers.Default) {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsWithToxicityFiltered")
        threadUtil.assertIsBackgroundThread()

        val list = plantList
        val resultList = mutableListOf<PresentablePlant>()
        filterJob?.cancelAndJoin()
        val filterJobLocal = launch(Dispatchers.Default) {
            eventLogger.log(Severity.DEBUG, classTag(), "Starting Job $this")
            for (plant in list) {
                if (!isActive)
                    break
                if (plant.scientificName.contains(query, true) || plant.mainCommonName.contains(query, true)) {
                    resultList.add(plant)
                }
            }
        }
        filterJob = filterJobLocal
        if (filterJobLocal.isCancelled) {
            eventLogger.log(Severity.DEBUG, classTag(), "Cancelling filterJob $filterJob")
            return@withContext null
        }

        eventLogger.log(Severity.DEBUG, classTag(), "Filtering returned ${resultList.size} results")
        return@withContext resultList.sortedBy { it.mainCommonName }
    }

    override fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata? {
        eventLogger.log(Severity.INFO, classTag(), "getPlantMetadata")
        threadUtil.assertIsBackgroundThread()

        val plantCustomEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null
        return PlantMetadata(plantId, animalType, plantCustomEntry.is_toxic, plantCustomEntry.description, plantCustomEntry.source)
    }
}
