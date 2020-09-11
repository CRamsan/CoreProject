package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.KeyItem

/**
 * Class that will build a [KeyItem]
 */
class KeyItemBuilder(
    id: String,
    name: String,
) : EntityBuilder(id, name) {

    fun build(entityId: Int, posX: Int, posY: Int) = KeyItem(entityId, posX, posY)

}