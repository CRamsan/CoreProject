package com.cramsan.ps2link.service.controller.domain

import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.controller.census.DBGServiceClient
import com.cramsan.ps2link.service.repository.mongo.CharacterRepository
import com.cramsan.ps2link.service.repository.mongo.models.Character
import com.cramsan.ps2link.service.toCensusModel
import com.cramsan.ps2link.service.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class CharacterController(
    private val characterRepository: CharacterRepository,
    private val dbgServiceClient: DBGServiceClient,
    private val clock: Clock,
    private val coroutineScope: CoroutineScope,
) {

    suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
        cacheBehaviour: CacheBehaviour = CacheBehaviour.USE_VALID,
        fetchBehaviour: FetchBehaviour = FetchBehaviour.BLOCKING_RETRIEVE,
    ): Character? {
        val cachedCharacter = characterRepository.getCharacter(characterId, namespace)

        val requestedCharacter = when (cacheBehaviour) {
            CacheBehaviour.USE_VALID -> {
                if (isCacheValid(cachedCharacter)) {
                    cachedCharacter
                } else {
                    null
                }
            }
            CacheBehaviour.USE_EXPIRED -> cachedCharacter
        }

        return when (fetchBehaviour) {
            FetchBehaviour.FORGET -> {
                requestedCharacter
            }
            FetchBehaviour.BLOCKING_RETRIEVE -> {
                getAndCacheCharacter(characterId, namespace)
            }
            FetchBehaviour.ASYNC_RETRIEVE -> {
                queueCharacterCaching(characterId, namespace)
                requestedCharacter
            }
        }
    }

    private fun queueCharacterCaching(characterId: String, namespace: Namespace) {
        coroutineScope.launch {
            getAndCacheCharacter(characterId, namespace)
        }
    }

    private suspend fun getAndCacheCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character? {
        val resultList = dbgServiceClient.getCharacter(characterId, namespace.toCensusModel())
        val fetchedCharacter = resultList?.character_list?.firstOrNull() ?: return null

        val entity = fetchedCharacter.toEntity(namespace)
        return characterRepository.updateCharacter(entity)
    }

    private fun isCacheValid(cachedCharacter: Character?): Boolean {
        if (cachedCharacter == null) {
            return false
        }

        val lastUpdate = Instant.fromEpochSeconds(cachedCharacter.lastUpdated)
        return clock.now() - lastUpdate < CACHE_EXPIRATION
    }

    companion object {
        private val CACHE_EXPIRATION = 5.minutes
    }
}
