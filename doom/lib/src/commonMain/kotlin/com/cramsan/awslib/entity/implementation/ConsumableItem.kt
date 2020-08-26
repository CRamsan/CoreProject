package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.GameItemInterface

/**
 * Class that represents an item that is used instantly on contact
 */
open class ConsumableItem(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
    val type: ConsumableType,
    val ammount: Int
) : GameItemInterface
