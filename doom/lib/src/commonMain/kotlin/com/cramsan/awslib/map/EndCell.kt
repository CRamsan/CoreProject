package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType

class EndCell : Cell(TerrainType.END) {
    override fun blocksMovement(): Boolean {
        return false
    }

    override fun onActionTaken() {
    }
}