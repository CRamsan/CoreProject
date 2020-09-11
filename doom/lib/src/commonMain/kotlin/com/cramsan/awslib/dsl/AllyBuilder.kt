package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.AllyType
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType

/**
 * Class that will build a [ConsumableItem]
 */
class AllyBuilder(
    id: String,
    name: String,
    val allyType: AllyType
) : EntityBuilder(id, name) {

    fun build(entityId: Int,
              posX: Int,
              posY: Int,
              priority: Int,
              enabled: Boolean): Ally {
        return Ally(entityId, posX, posY, priority, enabled)
    }
}