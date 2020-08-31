package com.cramsan.awslib.entity

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType

interface CharacterInterface : GameEntityInterface {
    var group: Int
    var health: Int
    var attack: Int
    val type: EntityType
    var nextTurnAction: TurnActionInterface
    var heading: Direction
    var enabled: Boolean
    var shouldMove: Boolean
}
