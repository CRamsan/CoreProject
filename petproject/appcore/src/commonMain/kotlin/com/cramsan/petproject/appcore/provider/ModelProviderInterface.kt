package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.PresentablePlant
import kotlinx.coroutines.flow.Flow

/**
 * Interface for a ModelProvider. The interface provides methods to access core model.
 * The implementation should take care of fetching new data and caching as needed.
 */
interface ModelProviderInterface {

    /**
     * Returns [true] when there is data cached and the last update time
     * is less than it's expiration time. The expiration time is decided by the implementation.
     */
    fun isCatalogAvailable(currentTime: Long): Boolean

    /**
     * Suspending function that will download a new catalog if [force] is true of if the cache
     * has expired. If there is no need to download the catalog, this function will return false.
     * When the catalog is downloaded, then this function will return true. This function may throw
     * on network error.
     */
    suspend fun downloadCatalog(currentTime: Long, force: Boolean = false): Boolean

    /**
     * Register [listener] to receive events from this instance.
     * The caller should ensure to call [deregisterForCatalogEvents].
     */
    fun registerForCatalogEvents(listener: ModelProviderEventListenerInterface)

    /**
     * Deregister the [listener] from receiving events from this instance.
     */
    fun deregisterForCatalogEvents(listener: ModelProviderEventListenerInterface)

    /**
     * Try to fetch a [Plant] from local cache. Return null if the no entry is found that
     * match the [animalType], [plantId] and [locale].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPlant(animalType: AnimalType, plantId: Int, locale: String): Plant?

    /**
     * Try to fetch a [PlantMetadata] from local cache. Return null if the no entry is found that
     * match the [animalType], [plantId] and [locale].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPlantMetadata(animalType: AnimalType, plantId: Int, locale: String): PlantMetadata?

    /**
     * Fetch the current count of entries that match the [animalType] and [locale].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPresentablePlantsCount(animalType: AnimalType, locale: String): Long

    /**
     * Fetch the list of [PresentablePlant] that match the [animalType] and [locale].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPlantsWithToxicity(animalType: AnimalType, locale: String): List<PresentablePlant>

    /**
     * Get a flow that emits the list of [PresentablePlant] that match the [animalType] and [locale].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    fun getPlantsWithToxicityFlow(animalType: AnimalType, locale: String): Flow<List<PresentablePlant>>

    /**
     * Fetch a paginated list of [PresentablePlant] that match the [animalType] and [locale].
     * The [limit] can be used to specify the size of the page to fetch. The [offset] is used to
     * specify the point to start fetching data from.
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPlantsWithToxicityPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<PresentablePlant>

    /**
     * Fetch the list of [PresentablePlant] that match the [animalType] and [locale] but filtered
     * by the provided [query].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun getPlantsWithToxicityFiltered(animalType: AnimalType, query: String, locale: String): List<PresentablePlant>

    /**
     * Get a flow that emits a list of [PresentablePlant] that match the [animalType] and [locale]
     * but filtered by the provided [query].
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    fun getPlantsWithToxicityFilteredFlow(animalType: AnimalType, query: String, locale: String): Flow<List<PresentablePlant>>

    /**
     * Clears the local cache. Calling this function will cause [isCatalogAvailable] to return false.
     *
     * @see isCatalogAvailable
     * @see downloadCatalog
     */
    suspend fun deleteAll()
}
