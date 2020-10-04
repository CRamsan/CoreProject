package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This base class is to be used to define an entity that has [health] and a [group].
 */
abstract class Character(
    id: String,
    override var health: Int,
    override var group: String,
    posX: Int,
    posY: Int,
    speed: Int,
    enabled: Boolean,
    shouldMove: Boolean
) :
    GameEntity(
        id,
        posX,
        posY,
        speed,
        enabled,
        shouldMove
    ),
    CharacterInterface {
    private var _attack = InitialValues.CHARACTER_ATTACK
    override var attack: Int
        get() = _attack
        set(value) {
            _attack = value
        }
}
