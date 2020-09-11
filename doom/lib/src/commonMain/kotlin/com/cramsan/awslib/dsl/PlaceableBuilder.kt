package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.*
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * Class that will build a [ConsumableItem]
 */
class PlaceableBuilder(
    id: String,
    name: String,
) : EntityBuilder(id, name) {

    fun build(entityId: Int,
              posX: Int,
              posY: Int,
              priority: Int,
              enabled: Boolean): GameEntity {
        return Enemy(entityId, posX, posY, priority, enabled)
    }

}