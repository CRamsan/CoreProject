package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This class implements [com.cramsan.awslib.entity.implementation.Character] with defaults that
 * make it a friendly character.
 */
open class Ally(
    id: String,
    health: Int,
    posX: Int,
    posY: Int,
    priority: Int,
    enabled: Boolean,
    val allyType: AllyType,
) :
    Character(
        id,
        health,
        InitialValues.GROUP_PLAYER,
        posX,
        posY,
        priority,
        enabled,
        false
    ) {

    override var attack = InitialValues.ATTACK_DOCTOR
}
