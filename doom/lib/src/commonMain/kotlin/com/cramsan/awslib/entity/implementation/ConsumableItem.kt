package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.ItemInterface

/**
 * Class that represents an item that is used instantly on contact
 */
class ConsumableItem(
    override val id: String,
    override var posX: Int,
    override var posY: Int,
    val type: ConsumableType,
    val ammount: Int
) : ItemInterface