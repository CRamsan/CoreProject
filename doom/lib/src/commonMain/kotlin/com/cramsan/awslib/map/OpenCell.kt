package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType

class OpenCell : Cell(TerrainType.OPEN) {

    override fun blocksMovement(): Boolean {
        return false
    }

    override fun onActionTaken() {
    }
}