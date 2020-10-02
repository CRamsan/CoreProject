package com.cramsan.awslib.utils.map

import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.CellFactory
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

actual class MapLoader {
    actual fun loadCSVMap(resourceName: String): Array<Array<Cell>> {
        val encoding = Charset.defaultCharset()
        val inputStream = FileInputStream(resourceName)
        val reader = InputStreamReader(inputStream, encoding)
        val buffer = BufferedReader(reader)

        val terrainTypeMap = mutableListOf<List<TerrainType>>()

        var readResult = buffer.readLine()
        while (readResult != null) {
            val tokens = readResult.split(",")
            var terrainTypeArray = mutableListOf<TerrainType>()
            tokens.forEach {
                terrainTypeArray.add(
                    when (it) {
                        "0" -> TerrainType.OPEN
                        "1" -> TerrainType.WALL
                        "2" -> TerrainType.DOOR
                        "3" -> TerrainType.END
                        else -> TerrainType.WALL
                    }
                )
            }
            terrainTypeMap.add(terrainTypeArray)
            readResult = buffer.readLine()
        }

        var gameMap = arrayOf<Array<Cell>>()
        for (column in 0 until terrainTypeMap.first().size) {
            val cellColumn = mutableListOf<Cell>()
            for (row in 0 until terrainTypeMap.size) {
                val value = terrainTypeMap[row][column]
                val mapColumn = CellFactory.createCell(value)
                cellColumn += mapColumn
            }
            gameMap += cellColumn.toTypedArray()
        }

        return gameMap
    }
}
