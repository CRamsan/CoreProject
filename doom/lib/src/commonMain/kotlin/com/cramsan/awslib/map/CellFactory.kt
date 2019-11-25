package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType

object CellFactory {

    fun createCell(terrainType: TerrainType): Cell {
        return when (terrainType) {
            TerrainType.OPEN -> OpenCell()
            TerrainType.WALL -> WallCell()
            TerrainType.DOOR -> DoorCell()
            TerrainType.END -> EndCell()
        }
    }
}