package com.cramsan.awslib.entity

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.map.GridPositionableInterface

/**
 * This interface defines a contract for elements that are displayed in the [com.cramsan.awslib.map.GameMap] and that
 * can be interacted on.
 */
interface GameEntityInterface : GridPositionableInterface {
    val id: Int
    var group: Int
    var health: Int
    var attack: Int
    val type: EntityType
    var nextTurnAction: TurnActionInterface
    var heading: Direction
    var enabled: Boolean
    var shouldMove: Boolean
}