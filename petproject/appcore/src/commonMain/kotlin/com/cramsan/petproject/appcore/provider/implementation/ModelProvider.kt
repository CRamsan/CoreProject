package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.ProviderConfig
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.DescriptionImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantCommonNameImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantFamilyImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantImp
import com.cramsan.petproject.appcore.storage.implementation.PlantMainNameImpl
import com.cramsan.petproject.appcore.storage.implementation.ToxicityImpl
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
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

    val config: ProviderConfig? = null

    private val http: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }

    private lateinit var plantList: List<PresentablePlant>
    private val listeners = mutableSetOf<ModelProviderEventListenerInterface>()
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
        eventLogger.log(Severity.INFO, "ModelProvider", "downloadCatalog")
        threadUtil.assertIsBackgroundThread()

        mutex.withLock {
            val lastSave = preferences.loadLong(LAST_UPDATE)
            if (lastSave != null && currentTime - lastSave < 86400) {
                eventLogger.log(Severity.INFO, "ModelProvider", "Using cached data")
                isCatalogReady = true
                return false
            }

            eventLogger.log(Severity.INFO, "ModelProvider", "Downloading data")
            listeners.forEach {
                it.onCatalogUpdate(false)
            }

            val plants: ArrayList<PlantImp> = http.get("https://petproject-api.azurewebsites.net/api/plants?code=YPRgK2Aw13tryQgemoSJFVSrHVgSajAhYZ3y2bKYgKb2uzmDZYo2bA==")
            modelStorage.insertPlantList(plants)
            val mainNames: ArrayList<PlantMainNameImpl> = http.get("https://petproject-api.azurewebsites.net/api/name/main?code=MrCJgPbvCsHo9VDa856Y9sOo/Gc0Rq80tbucVt2u6c2hJbuIzy/0Fg==")
            modelStorage.insertPlantMainNameList(mainNames)
            val toxicities: ArrayList<ToxicityImpl> = http.get("https://petproject-api.azurewebsites.net/api/toxicity?code=9DkhZP7t7X8NmgPrafGQcqkWJ5S57wpeZnPu2EjAugS7hB2AgfKpgQ==")
            modelStorage.insertToxicityList(toxicities)
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
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlant")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
            return null
        }

        var plantEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale)

        if (plantEntry == null) {
            try {
                val commonName: ArrayList<PlantCommonNameImpl> = http.get("https://petproject-api.azurewebsites.net/api/name/common/$plantId?code=rl0wZmFlhUBtv2xuCg4ClCgJ3PJk291dQJadO0TO9P5V83fWn3ZV3g==")
                commonName.forEach {
                    modelStorage.insertPlantCommonName(it)
                }
            } catch (cause: ClientRequestException) {
                eventLogger.log(Severity.WARNING, "ModelProvider", cause.toString())
            }

            val family: PlantFamilyImpl = http.get("https://petproject-api.azurewebsites.net/api/family/$plantId?code=5ojL7jdhEAO9Z7TLO6mJspal894Zi05iICKlDxixeKB60OsnBLH6Lw==")
            val description: DescriptionImpl = http.get("https://petproject-api.azurewebsites.net/api/description/${plantId}/${animalType.ordinal}?code=jFXlsMaMH2cAJ6SV7t8caftgURa7rLOEvXnxsamRZVCq4QYj3Xgi6g==")
            modelStorage.insertPlantFamily(family)
            modelStorage.insertDescription(description)
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
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlantsWithToxicity")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
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
                    it.animalId ?: AnimalType.ALL,
                    it.isToxic ?: ToxicityValue.UNDETERMINED
                ))
        }
        plantList = mutableList.sortedBy { it.mainCommonName }
        return plantList
    }

    override suspend fun getPlantsWithToxicityPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlantsWithToxicityPaginated")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
            return emptyList()
        }

        val list = modelStorage.getCustomPlantEntriesPaginated(animalType, locale, limit, offset)
        val page = mutableListOf<PresentablePlant>()
        list.forEach {
            page.add(
                PresentablePlant(
                    it.id,
                    it.scientificName,
                    it.mainName,
                    it.animalId ?: AnimalType.ALL,
                    it.isToxic ?: ToxicityValue.UNDETERMINED
                ))
        }
        return page
    }

    override suspend fun getPlantsWithToxicityFiltered(
        animalType: AnimalType,
        query: String,
        locale: String
    ): List<PresentablePlant>? = withContext(Dispatchers.Default) {
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlantsWithToxicityFiltered")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
            return@withContext null
        }

        val list = plantList
        val resultList = mutableListOf<PresentablePlant>()
        filterJob?.cancelAndJoin()
        val filterJobLocal = launch(Dispatchers.Default) {
            eventLogger.log(Severity.DEBUG, "ModelProvider", "Starting Job $this")
            for (plant in list) {
                if (!isActive) {
                    eventLogger.log(Severity.DEBUG, "ModelProvider", "Early cancelling Job $this")
                    break
                }
                if (plant.scientificName.contains(query, true) || plant.mainCommonName.contains(query, true)) {
                    resultList.add(plant)
                }
            }
        }
        filterJob = filterJobLocal
        if (filterJobLocal.isCancelled) {
            eventLogger.log(Severity.DEBUG, "ModelProvider", "Cancelling filterJob $filterJob")
            return@withContext null
        }
        filterJobLocal.join()
        eventLogger.log(Severity.DEBUG, "ModelProvider", "Filtering returned ${resultList.size} results")
        return@withContext resultList.sortedBy { it.mainCommonName }
    }

    override suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata? {
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlantMetadata")
        threadUtil.assertIsBackgroundThread()

        val plantCustomEntry = modelStorage.getCustomPlantEntry(animalType, plantId, locale) ?: return null
        return PlantMetadata(plantId, animalType, plantCustomEntry.isToxic, plantCustomEntry.description, plantCustomEntry.source)
    }

    override suspend fun getPresentablePlantsCount(animalType: AnimalType, locale: String): Long {
        eventLogger.log(Severity.INFO, "ModelProvider", "getPresentablePlantsCount")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
            return if (::plantList.isInitialized) {
                plantList.size.toLong()
            } else {
                0
            }
        }

        return modelStorage.getPlantCount()
    }

    override suspend fun deleteAll() {
        eventLogger.log(Severity.INFO, "ModelProvider", "deleteAll")
        threadUtil.assertIsBackgroundThread()

        modelStorage.deleteAll()
        preferences.saveLong(LAST_UPDATE, 0)
        isCatalogReady = false
    }
}
