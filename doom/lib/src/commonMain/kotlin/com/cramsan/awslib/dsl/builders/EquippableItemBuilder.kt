package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.EquippableType

/**
 * Class that will build an [EquippableItem]
 */
class EquippableItemBuilder(
    templateName: String,
    private val range: Int,
    private val accuracy: Double,
    private val damage: Double,
    private val type: EquippableType
) : EntityBuilder(templateName) {

    fun build(entityId: String, posX: Int, posY: Int) =
        EquippableItem(entityId, posX, posY, range, accuracy, damage, type)
}
