package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * Class that will build an [EquippablItem]
 */
class EquippableItemBuilder(
    id: String,
    name: String,
    var range: Int,
    var accuracy: Double,
    var damage: Double
) : EntityBuilder(id, name) {

    fun build(entityId: Int, posX: Int, posY: Int) = EquippableItem(entityId, posX, posY)

}