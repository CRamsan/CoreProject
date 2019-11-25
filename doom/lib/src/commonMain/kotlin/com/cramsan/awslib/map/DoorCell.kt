package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType

class DoorCell(var opened: Boolean = false) : Cell(TerrainType.DOOR) {
    override fun blocksMovement(): Boolean {
        return opened.not()
    }

    override fun onActionTaken() {
        opened = opened.not()
    }
}