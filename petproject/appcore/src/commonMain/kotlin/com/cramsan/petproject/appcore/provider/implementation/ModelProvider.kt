package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.utils.format.StringFormatter
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import kotlin.coroutines.coroutineContext
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class ModelProvider(
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface,
    private val modelStorage: ModelStorageInterface,
    private val preferences: Preferences,
    private val config: ProviderConfig
) : ModelProviderInterface {

    private val http: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }

    private val listeners = mutableSetOf<ModelProviderEventListenerInterface>()
    var isCatalogReady = false
    private val mutex = Mutex()
    private val LAST_UPDATE = "LastUpdate"

    @OptIn(ExperimentalTime::class)
    override fun isCatalogAvailable(currentTime: Long): Boolean {
        if (isCatalogReady)
            return true
        val lastSave = preferences.loadLong(LAST_UPDATE)
        if (lastSave != null) {
            val now = Instant.fromEpochMilliseconds(currentTime)
            val lastSaveTime = Instant.fromEpochMilliseconds(lastSave)

            if (now.minus(lastSaveTime).toDouble(DurationUnit.DAYS) > 30) {
                return false
            }

            isCatalogReady = true
            listeners.forEach {
                it.onCatalogUpdate(true)
            }
            return true
        }
        return false
    }

    override suspend fun downloadCatalog(currentTime: Long, force: Boolean): Boolean {
        eventLogger.log(Severity.INFO, "ModelProvider", "downloadCatalog")
        threadUtil.assertIsBackgroundThread()

        mutex.withLock {
            val lastSave = preferences.loadLong(LAST_UPDATE)
            if (!force && lastSave != null && currentTime - lastSave < 259200) {
                eventLogger.log(Severity.INFO, "ModelProvider", "Using cached data")
                setIsCatalogReady(true)
                return false
            }

            eventLogger.log(Severity.INFO, "ModelProvider", "Downloading data")
            setIsCatalogReady(false)

            coroutineScope {
                launch {
                    val plants: ArrayList<PlantImp> = http.get(config.plantsEndpoint)
                    modelStorage.insertPlantList(plants)
                }
                launch {
                    val mainNames: ArrayList<PlantMainNameImpl> =
                        http.get(config.mainNameEndpoint)
                    modelStorage.insertPlantMainNameList(mainNames)
                }
                launch {
                    val toxicities: ArrayList<ToxicityImpl> = http.get(config.toxicityEndpoint)
                    modelStorage.insertToxicityList(toxicities)
                }
            }

            setIsCatalogReady(true)
            eventLogger.log(Severity.INFO, "ModelProvider", "Data downloaded")
            preferences.saveLong(LAST_UPDATE, currentTime)
        }
        return true
    }

    private fun setIsCatalogReady(isReady: Boolean) {
        if (isCatalogReady != isReady) {
            listeners.forEach {
                it.onCatalogUpdate(isReady)
            }
        }
        eventLogger.log(Severity.INFO, "ModelProvider", "IsCatalogReady = $isReady")
        isCatalogReady = isReady
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

        val formatter = StringFormatter()
        if (plantEntry == null) {
            try {

                val commonNameEndpoint = formatter.format(config.commonNamesEndpoint, plantId)
                val commonName: ArrayList<PlantCommonNameImpl> = http.get(commonNameEndpoint)
                commonName.forEach {
                    modelStorage.insertPlantCommonName(it)
                }
            } catch (cause: ClientRequestException) {
                eventLogger.log(Severity.WARNING, "ModelProvider", cause.toString())
            }

            try {
                val familyEndpoint = formatter.format(config.familyEndpoint, plantId)
                val family: PlantFamilyImpl = http.get(familyEndpoint)
                val descriptionEndpoint = formatter.format(config.descriptionsEndpoint, plantId, animalType.ordinal)
                val description: DescriptionImpl = http.get(descriptionEndpoint)
                modelStorage.insertPlantFamily(family)
                modelStorage.insertDescription(description)
            } catch (cause: ClientRequestException) {
                eventLogger.log(Severity.ERROR, "ModelProvider", cause.toString())
                return null
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
                )
            )
        }
        return mutableList.sortedBy { it.mainCommonName }
    }

    override fun getPlantsWithToxicityFlow(
        animalType: AnimalType,
        locale: String
    ): Flow<List<PresentablePlant>> {
        TODO("Not yet implemented")
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
                )
            )
        }
        return page
    }

    override suspend fun getPlantsWithToxicityFiltered(
        animalType: AnimalType,
        query: String,
        locale: String
    ): List<PresentablePlant> {
        eventLogger.log(Severity.INFO, "ModelProvider", "getPlantsWithToxicityFiltered")
        threadUtil.assertIsBackgroundThread()

        if (!isCatalogReady) {
            eventLogger.log(Severity.INFO, "ModelProvider", "Catalog is not ready")
            return emptyList()
        }

        val list = getPlantsWithToxicity(animalType, locale)
        val resultList = mutableListOf<PresentablePlant>()
        for (plant in list) {
            if (!coroutineContext.isActive) {
                eventLogger.log(Severity.DEBUG, "ModelProvider", "Early cancelling Job $this")
                break
            }
            if (plant.scientificName.contains(query, true) || plant.mainCommonName.contains(query, true)) {
                resultList.add(plant)
            }
        }
        eventLogger.log(Severity.DEBUG, "ModelProvider", "Filtering returned ${resultList.size} results")
        return resultList.sortedBy { it.mainCommonName }
    }

    override fun getPlantsWithToxicityFilteredFlow(
        animalType: AnimalType,
        query: String,
        locale: String
    ): Flow<List<PresentablePlant>> {
        TODO("Not yet implemented")
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
            return 0
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
