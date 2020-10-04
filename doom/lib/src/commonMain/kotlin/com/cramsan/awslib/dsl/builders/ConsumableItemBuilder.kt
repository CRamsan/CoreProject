package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType

/**
 * Class that will build a [ConsumableItem]
 */
class ConsumableItemBuilder(
    templateName: String,
    private val type: ConsumableType,
    private val amount: Int,
) : EntityBuilder(templateName) {

    fun build(entityId: String, posX: Int, posY: Int) = ConsumableItem(entityId, posX, posY, type, amount)
}
