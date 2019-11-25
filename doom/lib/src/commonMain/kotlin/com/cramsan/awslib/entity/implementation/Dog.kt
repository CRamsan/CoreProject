package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This class implements [com.cramsan.awslib.entity.implementation.Character] with defaults that can be used as a
 * generic enemy.
 */
open class Dog(
    id: Int,
    posX: Int,
    posY: Int,
    priority: Int,
    enabled: Boolean
) :
    Character(id,
            InitialValues.HEALTH_DOG,
            InitialValues.ENEMY_GROUP,
            posX,
            posY,
            EntityType.DOG,
            priority,
            enabled,
            true) {

    override var attack = InitialValues.ATTACK_DOG
}