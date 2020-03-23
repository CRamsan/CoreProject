package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This class implements [com.cramsan.awslib.entity.implementation.Character] with defaults that can be used as a friendly
 * or foe scientist.
 */
open class Doctor(
    id: Int,
    group: Int,
    posX: Int,
    posY: Int,
    priority: Int,
    enabled: Boolean
) :
    Character(id,
            InitialValues.HEALTH_DOCTOR,
            group,
            posX,
            posY,
            EntityType.SCIENTIST,
            priority,
            enabled,
            false) {

    override var attack = InitialValues.ATTACK_DOCTOR
}
