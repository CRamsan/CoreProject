package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.KeyItem

/**
 * Class that will build a [KeyItem]
 */
class KeyItemBuilder(
    templateName: String,
) : EntityBuilder(templateName) {

    fun build(entityId: String, posX: Int, posY: Int) = KeyItem(entityId, posX, posY)
}
