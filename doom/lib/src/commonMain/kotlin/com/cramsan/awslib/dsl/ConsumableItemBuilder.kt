package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType

/**
 * Class that will build a [ConsumableItem]
 */
class ConsumableItemBuilder(
    id: String,
    name: String,
    val type: ConsumableType,
    val amount: Int,
) : EntityBuilder(id, name) {

    fun build(entityId: Int, posX: Int, posY: Int) = ConsumableItem(entityId, posX, posY, type, amount)
}