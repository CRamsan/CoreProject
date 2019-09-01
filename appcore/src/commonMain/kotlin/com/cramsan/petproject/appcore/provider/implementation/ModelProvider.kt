package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.http.HttpInterface
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
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModelProvider(
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface,
    private val modelStorage: ModelStorageInterface,
    private val http: HttpInterface
) : ModelProviderInterface {

    lateinit var plantList: List<PresentablePlant>
    var filterJob: Job? = null

    override suspend fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant? {
        eventLogger.log(Severity.INFO, classTag(), "getPlant")
        threadUtil.assertIsBackgroundThread()

        val plantEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null

        return Plant(
            plantEntry.id.toInt(),
            plantEntry.scientificName,
            plantEntry.mainName,
            plantEntry.commonNames,
            plantEntry.imageUrl,
            plantEntry.family
        )
    }

    override suspend fun getPlants(animalType: AnimalType, locale: String): List<Plant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlants")
        threadUtil.assertIsBackgroundThread()

        val list = modelStorage.getCustomPlantsEntries(animalType, locale)
        val mutableList = mutableListOf<Plant>()

        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientificName,
                    it.mainName,
                    "",
                    it.imageUrl,
                    it.family
                ))
        }
        return mutableList
    }

    override suspend fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsWithToxicity")
        threadUtil.assertIsBackgroundThread()

        var list = modelStorage.getCustomPlantsEntries(animalType, locale)
        val mutableList = mutableListOf<PresentablePlant>()

        if (list.isEmpty()) {
            downloadDatabaseEntries()
            list = modelStorage.getCustomPlantsEntries(animalType, locale)
        }

        list.forEach {
            mutableList.add(
                PresentablePlant(
                    it.id,
                    it.scientificName,
                    it.mainName,
                    it.isToxic ?: ToxicityValue.UNDETERMINED
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

    override suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata? {
        eventLogger.log(Severity.INFO, classTag(), "getPlantMetadata")
        threadUtil.assertIsBackgroundThread()

        val plantCustomEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null
        return PlantMetadata(plantId, animalType, plantCustomEntry.isToxic, plantCustomEntry.description, plantCustomEntry.source)
    }

    suspend fun downloadDatabaseEntries() {
        val plants = http.get("", listOf<com.cramsan.petproject.appcore.storage.Plant>()::class)
        val mainNames = http.get("", listOf<PlantMainName>()::class)
        val commonNames = http.get("", listOf<PlantCommonName>()::class)
        val families = http.get("", listOf<PlantFamily>()::class)
        val descriptions = http.get("", listOf<Description>()::class)
        val toxicities = http.get("", listOf<Toxicity>()::class)

        plants.forEach {
            modelStorage.insertPlant(it)
        }
        mainNames.forEach {
            modelStorage.insertPlantMainName(it)
        }
        commonNames.forEach {
            modelStorage.insertPlantCommonName(it)
        }
        families.forEach {
            modelStorage.insertPlantFamily(it)
        }
        descriptions.forEach {
            modelStorage.insertDescription(it)
        }
        toxicities.forEach {
            modelStorage.insertToxicity(it)
        }
    }
}
