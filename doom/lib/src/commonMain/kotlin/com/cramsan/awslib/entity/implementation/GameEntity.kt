package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType

/**
 * Abstract implementation for all elements that will need to be displayed in the [com.cramsan.awslib.map.GameMap].
 */

abstract class GameEntity(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
    override val type: EntityType,
    var speed: Int,
    override var enabled: Boolean,
    override var shouldMove: Boolean
) : GameEntityInterface {
    override var heading = Direction.NORTH
    override var nextTurnAction = TurnAction.NOOP

    override fun toString(): String {
        return "Id: $id, X: $posX, Y: $posY, Type: $type"
    }
}
