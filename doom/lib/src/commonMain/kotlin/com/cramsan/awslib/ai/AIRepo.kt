package com.cramsan.awslib.ai.`interface`

import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.map.GameMap

/**
 * Interface that exposes a simple API to get next move.
 */
interface AIRepo {

    /**
     * Take the current state of the game from the [entity], [entityManager] and [map] and returns a [TurnActionInterface]
     */
    fun getNextTurnAction(entity: GameEntityInterface, entityManager: EntityManager, map: GameMap): TurnActionInterface
}
