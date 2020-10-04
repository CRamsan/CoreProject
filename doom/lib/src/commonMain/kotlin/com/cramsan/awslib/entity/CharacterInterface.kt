package com.cramsan.awslib.entity

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.enums.Direction

interface CharacterInterface : GameEntityInterface {
    var group: String
    var health: Int
    var attack: Int
    var nextTurnAction: TurnActionInterface
    var heading: Direction
    var enabled: Boolean
    var shouldMove: Boolean
}
