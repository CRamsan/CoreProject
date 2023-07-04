package com.cramsan.ps2link.service.controller.domain

import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.controller.census.DBGServiceClient
import com.cramsan.ps2link.service.repository.mongo.ItemRepository
import com.cramsan.ps2link.service.repository.mongo.models.Item
import com.cramsan.ps2link.service.toCensusModel
import com.cramsan.ps2link.service.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

class ItemController(
    private val itemRepository: ItemRepository,
    private val dbgServiceClient: DBGServiceClient,
    private val clock: Clock,
    private val coroutineScope: CoroutineScope,
) {

    suspend fun getItem(
        itemId: String,
        namespace: Namespace,
        cacheBehaviour: CacheBehaviour = CacheBehaviour.USE_VALID,
        fetchBehaviour: FetchBehaviour = FetchBehaviour.BLOCKING_RETRIEVE,
    ): Item? {
        val cachedItem = itemRepository.getItem(itemId, namespace)

        val requestedItem = when (cacheBehaviour) {
            CacheBehaviour.USE_VALID -> {
                if (isCacheValid(cachedItem)) {
                    cachedItem
                } else {
                    null
                }
            }
            CacheBehaviour.USE_EXPIRED -> cachedItem
        }

        return when (fetchBehaviour) {
            FetchBehaviour.FORGET -> {
                requestedItem
            }
            FetchBehaviour.BLOCKING_RETRIEVE -> {
                getAndCacheCharacter(itemId, namespace)
            }
            FetchBehaviour.ASYNC_RETRIEVE -> {
                queueCharacterCaching(itemId, namespace)
                requestedItem
            }
        }
    }

    private fun queueCharacterCaching(characterId: String, namespace: Namespace) {
        coroutineScope.launch {
            getAndCacheCharacter(characterId, namespace)
        }
    }

    private suspend fun getAndCacheCharacter(
        itemId: String,
        namespace: Namespace,
    ): Item? {
        val resultList = dbgServiceClient.getItem(itemId, namespace.toCensusModel())
        val fetchedItem = resultList?.item_list?.firstOrNull() ?: return null

        val entity = fetchedItem.toEntity(namespace)
        return itemRepository.updateItem(entity)
    }

    private fun isCacheValid(cachedItem: Item?): Boolean {
        if (cachedItem == null) {
            return false
        }

        val lastUpdate = Instant.fromEpochSeconds(cachedItem.lastUpdated)
        return clock.now() - lastUpdate < CACHE_EXPIRATION
    }

    companion object {
        private val CACHE_EXPIRATION = 2.days
    }
}
