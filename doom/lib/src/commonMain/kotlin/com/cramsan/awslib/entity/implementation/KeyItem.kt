package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.GameItemInterface

/**
 * Class that represents an item that is picked up and stored. These items do no have an event attached but are keep as
 * part of the player inventory.
 */
open class KeyItem(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
) : GameItemInterface
