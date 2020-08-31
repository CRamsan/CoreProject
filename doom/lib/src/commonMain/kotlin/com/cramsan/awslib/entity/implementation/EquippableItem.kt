package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.ItemInterface

/**
 * Class that represents an item that is picked up and can be equipped. These items do no have an event attached but are
 * used to enable/disable changes while they are equipped.
 */
open class EquippableItem(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
) : ItemInterface
