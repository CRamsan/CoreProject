package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This base class is to be used to define an entity that has [health] and a [group].
 */
abstract class Character(
    id: Int,
    override var health: Int,
    override var group: Int,
    posX: Int,
    posY: Int,
    type: EntityType,
    speed: Int,
    enabled: Boolean,
    shouldMove: Boolean
) :
    GameEntity(
        id,
        posX,
        posY,
        type,
        speed,
        enabled,
        shouldMove
    ) {
    private var _attack = InitialValues.CHARACTER_ATTACK
    override var attack: Int
        get() = _attack
        set(value) {
            _attack = value
        }
}
