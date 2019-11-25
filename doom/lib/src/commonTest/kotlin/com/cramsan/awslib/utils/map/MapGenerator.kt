package com.cramsan.awslib.utils.map

import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.CellFactory

object MapGenerator {

    val mapWithWalls = arrayOf(
            //      0  1  2  3  4  5  6  7  8
            arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1), // 0
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 1
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 2
            arrayOf(1, 0, 0, 1, 1, 1, 0, 0, 1), // 3
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 4
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 5
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 6
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 7
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 8
            arrayOf(1, 0, 1, 1, 1, 1, 1, 0, 1), // 9
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 10
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 11
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 12
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 13
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 14
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 15
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 16
            arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1)) // 17

    val mapWithWallsAndDoors = arrayOf(
            //      0  1  2  3  4  5  6  7  8
            arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1), // 0
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 1
            arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 1), // 2
            arrayOf(1, 2, 1, 1, 1, 1, 1, 2, 1), // 3
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 4
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 5
            arrayOf(1, 0, 0, 0, 2, 0, 0, 0, 1), // 6
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 7
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 8
            arrayOf(1, 1, 1, 1, 1, 1, 1, 2, 1), // 9
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 10
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 11
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 12
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 13
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 14
            arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1), // 15
            arrayOf(1, 0, 0, 0, 2, 0, 0, 0, 1), // 16
            arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1)) // 17

    fun createMap(width: Int, height: Int): Array<Array<Cell>> {
        return Array(width) { Array(height) { CellFactory.createCell(TerrainType.OPEN) } }
    }

    fun createMap100x100(): Array<Array<Cell>> {
        return createMap(100, 100)
    }

    fun createMapWithWalls(): Array<Array<Cell>> {
        var gameMap = arrayOf<Array<Cell>>()

        for (column in 0 until mapWithWalls.first().size) {
            val cellColumn = mutableListOf<Cell>()
            for (row in 0 until mapWithWalls.size) {
                val value = mapWithWalls[row][column]
                val mapColumn = CellFactory.createCell(TerrainType.values()[value])
                cellColumn += mapColumn
            }
            gameMap += cellColumn.toTypedArray()
        }

        return gameMap
    }
}