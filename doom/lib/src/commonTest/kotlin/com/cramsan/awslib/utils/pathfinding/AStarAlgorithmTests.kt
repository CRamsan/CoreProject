package com.cramsan.awslib

import com.cramsan.awslib.utils.pathfinding.AStarAlgorithm
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.map.GameMapAStarFunctionProvider
import com.cramsan.awslib.utils.map.MapGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

class AStarAlgorithmTests {

    private lateinit var map: GameMap

    @Test
    fun findPathTestSmallMap() {
        map = GameMap(MapGenerator.createMap(3, 3))
        val cell1x1 = map.cellAt(1, 1)
        val cell0x0 = map.cellAt(0, 0)
        val cell1x0 = map.cellAt(1, 0)
        val cell0x1 = map.cellAt(0, 1)
        val cell2x2 = map.cellAt(2, 2)
        val functionProvider = GameMapAStarFunctionProvider()

        assertEquals(1, AStarAlgorithm.findPath(cell1x1, cell1x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(2, AStarAlgorithm.findPath(cell1x1, cell0x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(3, AStarAlgorithm.findPath(cell1x1, cell0x0, Int.MAX_VALUE, functionProvider).size)
        assertEquals(4, AStarAlgorithm.findPath(cell1x0, cell2x2, Int.MAX_VALUE, functionProvider).size)
    }

    @Test
    fun findPathTestMediumMap() {
        map = GameMap(MapGenerator.createMap(5, 5))
        val cell1x1 = map.cellAt(1, 1)
        val cell0x0 = map.cellAt(0, 0)
        val cell1x0 = map.cellAt(1, 0)
        val cell0x1 = map.cellAt(0, 1)
        val cell2x2 = map.cellAt(2, 2)
        val cell4x0 = map.cellAt(4, 0)
        val cell0x4 = map.cellAt(0, 4)
        val cell4x4 = map.cellAt(4, 4)
        val functionProvider = GameMapAStarFunctionProvider()

        // Run small map tests
        assertEquals(1, AStarAlgorithm.findPath(cell1x1, cell1x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(2, AStarAlgorithm.findPath(cell1x1, cell0x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(3, AStarAlgorithm.findPath(cell1x1, cell0x0, Int.MAX_VALUE, functionProvider).size)
        assertEquals(4, AStarAlgorithm.findPath(cell1x0, cell2x2, Int.MAX_VALUE, functionProvider).size)

        // Run medium map tests
        assertEquals(5, AStarAlgorithm.findPath(cell1x1, cell4x0, Int.MAX_VALUE, functionProvider).size)
        assertEquals(7, AStarAlgorithm.findPath(cell1x1, cell4x4, Int.MAX_VALUE, functionProvider).size)
        assertEquals(5, AStarAlgorithm.findPath(cell0x4, cell2x2, Int.MAX_VALUE, functionProvider).size)
        assertEquals(4, AStarAlgorithm.findPath(cell1x0, cell4x0, Int.MAX_VALUE, functionProvider).size)
    }

    @Test
    fun findPathTestMapWithWalls() {
        map = GameMap(MapGenerator.createMapWithWalls())
        val cell0x0 = map.cellAt(0, 0)
        val cell0x1 = map.cellAt(0, 1)
        val cell1x1 = map.cellAt(1, 1)
        val cell3x4 = map.cellAt(3, 4)
        val cell5x4 = map.cellAt(5, 4)
        val cell3x10 = map.cellAt(3, 10)
        val cell5x14 = map.cellAt(5, 14)
        val cell7x16 = map.cellAt(7, 16)
        val cell8x17 = map.cellAt(8, 17)
        val functionProvider = GameMapAStarFunctionProvider()

        assertEquals(1, AStarAlgorithm.findPath(cell1x1, cell1x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(1, AStarAlgorithm.findPath(cell0x0, cell0x0, Int.MAX_VALUE, functionProvider).size)
        assertEquals(0, AStarAlgorithm.findPath(cell0x0, cell0x1, Int.MAX_VALUE, functionProvider).size)
        assertEquals(1, AStarAlgorithm.findPath(cell8x17, cell8x17, Int.MAX_VALUE, functionProvider).size)
        assertEquals(6, AStarAlgorithm.findPath(cell1x1, cell3x4, Int.MAX_VALUE, functionProvider).size)
        assertEquals(11, AStarAlgorithm.findPath(cell3x4, cell5x4, Int.MAX_VALUE, functionProvider).size)
        assertEquals(11, AStarAlgorithm.findPath(cell3x10, cell3x4, Int.MAX_VALUE, functionProvider).size)
        assertEquals(9, AStarAlgorithm.findPath(cell5x14, cell3x10, Int.MAX_VALUE, functionProvider).size)
        assertEquals(0, AStarAlgorithm.findPath(cell7x16, cell8x17, Int.MAX_VALUE, functionProvider).size)
        assertEquals(0, AStarAlgorithm.findPath(cell8x17, cell1x1, Int.MAX_VALUE, functionProvider).size)
    }
}