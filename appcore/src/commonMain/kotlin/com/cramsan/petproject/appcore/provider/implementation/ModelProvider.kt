package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ModelProvider(
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface,
    private val modelStorage: ModelStorageInterface,
    private val preferences: PreferencesInterface
) : ModelProviderInterface {

    private val http: HttpClient = HttpClient() {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }

    private lateinit var plantList: List<PresentablePlant>
    private val listeners = mutableListOf<ModelProviderEventListenerInterface>()
    private var filterJob: Job? = null
    var isCatalogReady = false
    private val mutex = Mutex()
    private val LAST_UPDATE = "LastUpdate"

    override fun isCatalogAvailable(currentTime: Long): Boolean {
        if (isCatalogReady)
            return true
        val lastSave = preferences.loadLong(LAST_UPDATE)
        if (lastSave != null && currentTime - lastSave < 86400) {
            isCatalogReady = true
            listeners.forEach {
                it.onCatalogUpdate(true)
            }
            return true
        }
        return false
    }

    override suspend fun downloadCatalog(currentTime: Long): Boolean {
        eventLogger.log(Severity.INFO, classTag(), "downloadCatalog")
        threadUtil.assertIsBackgroundThread()

        mutex.withLock {
            val lastSave = preferences.loadLong(LAST_UPDATE)
            if (lastSave != null && currentTime - lastSave < 86400) {
                eventLogger.log(Severity.INFO, classTag(), "Using cached data")
                isCatalogReady = true
                return false
            }

            eventLogger.log(Severity.INFO, classTag(), "Downloading data")
            listeners.forEach {
                it.onCatalogUpdate(false)
            }

            val plants: ArrayList<com.cramsan.petproject.appcore.storage.Plant.PlantImp> =
                http.get("https://cramsan.com/data/plant/")
            val mainNames: ArrayList<PlantMainName.PlantMainNameImpl> = http.get("https://cramsan.com/data/name/")
            val toxicities: ArrayList<Toxicity.ToxicityImpl> = http.get("https://cramsan.com/data/toxicity/")

            plants.forEach {
                modelStorage.insertPlant(it)
            }
            mainNames.forEach {
                modelStorage.insertPlantMainName(it)
            }
            toxicities.forEach {
                modelStorage.insertToxicity(it)
            }
            isCatalogReady = true
            preferences.saveLong(LAST_UPDATE, currentTime)
            listeners.forEach {
                it.onCatalogUpdate(true)
            }
        }
        return true
    }

    override fun registerForCatalogEvents(listener: ModelProviderEventListenerInterface) {
        listeners.add(listener)
    }

    override fun deregisterForCatalogEvents(listener: ModelProviderEventListenerInterface) {
        listeners.remove(listener)
    }

    override suspend fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant? {
        eventLogger.log(Severity.INFO, classTag(), "getPlant")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, classTag(), "Catalog is not ready")
            return null
        }

        var plantEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale)

        if (plantEntry == null) {
            try {
                val commonName: ArrayList<PlantCommonName.PlantCommonNameImpl> = http.get("https://cramsan.com/data/common_name/$plantId/")
                commonName.forEach {
                    modelStorage.insertPlantCommonName(it)
                }
            } catch (cause: Throwable) {
                eventLogger.log(Severity.WARNING, classTag(), cause.toString())
            }

            var family: PlantFamily? = null
            try {
                family = http.get("https://cramsan.com/data/family/$plantId/")
            } catch (cause: Throwable) {
                eventLogger.log(Severity.WARNING, classTag(), cause.toString())
            }

            var description: Description? = null
            try {
                description = http.get("https://cramsan.com/data/description/$plantId/${animalType.ordinal}")
            } catch (cause: Throwable) {
                eventLogger.log(Severity.WARNING, classTag(), cause.toString())
            }
            if (family != null) {
                modelStorage.insertPlantFamily(family)
            }
            if (description != null) {
                modelStorage.insertDescription(description)
            }
            plantEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null
        }

        return Plant(
            plantEntry.id.toInt(),
            plantEntry.scientificName,
            plantEntry.mainName,
            plantEntry.commonNames,
            plantEntry.imageUrl,
            plantEntry.family
        )
    }

    override suspend fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, classTag(), "getPlantsWithToxicity")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, classTag(), "Catalog is not ready")
            return emptyList()
        }

        val list = modelStorage.getCustomPlantsEntries(animalType, locale)
        val mutableList = mutableListOf<PresentablePlant>()

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

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, classTag(), "Catalog is not ready")
            return@withContext null
        }

        val list = plantList
        val resultList = mutableListOf<PresentablePlant>()
        filterJob?.cancelAndJoin()
        val filterJobLocal = launch(Dispatchers.Default) {
            eventLogger.log(Severity.DEBUG, classTag(), "Starting Job $this")
            for (plant in list) {
                if (!isActive) {
                    eventLogger.log(Severity.DEBUG, classTag(), "Early cancelling Job $this")
                    break
                }
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
        filterJobLocal.join()
        eventLogger.log(Severity.DEBUG, classTag(), "Filtering returned ${resultList.size} results")
        return@withContext resultList.sortedBy { it.mainCommonName }
    }

    override suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata? {
        eventLogger.log(Severity.INFO, classTag(), "getPlantMetadata")
        threadUtil.assertIsBackgroundThread()

        val plantCustomEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null
        return PlantMetadata(plantId, animalType, plantCustomEntry.isToxic, plantCustomEntry.description, plantCustomEntry.source)
    }
}
