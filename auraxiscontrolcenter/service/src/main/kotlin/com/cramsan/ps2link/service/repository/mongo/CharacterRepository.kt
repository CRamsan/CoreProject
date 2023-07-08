package com.cramsan.ps2link.service.repository.mongo

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.repository.mongo.models.Character
import kotlinx.datetime.Clock
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.upsert

/**
 * Class for our articles database queries
 */
class CharacterRepository(
    private val characters: CoroutineCollection<Character>,
    private val clock: Clock,
) {
    /**
     * Query to create a new article
     */
    suspend fun createCharacter(character: Character): Character? {
        logI(TAG, "Create Character ${character.characterId}")
        val toBeInserted = character.setLastUpdateField()

        return if (characters.insertOne(toBeInserted).wasAcknowledged()) {
            toBeInserted
        } else {
            null
        }
    }

    /**
     * Query for an article by ID
     */
    suspend fun getCharacter(characterId: String, namespace: Namespace): Character? {
        logI(TAG, "Get Character $characterId")
        return characters.findOne(
            Character::characterId eq characterId,
            Character::namespace eq namespace,
        )
    }

    /**
     * Query to update an existing article, if it exists. False returned if
     * article not found with the ID
     */
    suspend fun updateCharacter(character: Character): Character? {
        logI(TAG, "Update Character ${character.characterId}")
        val toBeInserted = character.setLastUpdateField()

        return if (characters.updateOne(
                and(
                        Character::characterId eq character.characterId,
                        Character::namespace eq character.namespace,
                    ),
                toBeInserted,
                options = upsert()
            ).wasAcknowledged()
        ) {
            toBeInserted
        } else {
            null
        }
    }

    /**
     * Delete article from the db if the ID is found.
     */
    suspend fun deleteCharacter(characterId: String, namespace: Namespace): Boolean {
        logI(TAG, "Delete Character $characterId")
        return characters.deleteOne(
            and(
                Character::characterId eq characterId,
                Character::namespace eq namespace,
            )
        ).wasAcknowledged()
    }

    private fun Character.setLastUpdateField(): Character {
        return copy(lastUpdated = clock.now().epochSeconds)
    }

    companion object {
        private const val TAG = "CharacterRepository"
    }
}
