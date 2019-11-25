package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType

class WallCell : Cell(TerrainType.WALL) {

    override fun blocksMovement(): Boolean {
        return true
    }

    override fun onActionTaken() {
    }
}