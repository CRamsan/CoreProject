package com.cramsan.awslib.ai.`interface`

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.map.GameMap

/**
 * Interface that exposes a simple API to get next move.
 */
interface AIRepo {

    /**
     * Take the current state of the game from the [entity], [entityManager] and [map] and returns a [TurnActionInterface]
     */
    fun getNextTurnAction(character: CharacterInterface, entityManager: EntityManager, map: GameMap): TurnActionInterface
}
