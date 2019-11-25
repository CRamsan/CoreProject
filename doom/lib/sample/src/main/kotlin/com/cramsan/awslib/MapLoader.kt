package com.cramsan.awslib

import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.CellFactory
import java.nio.charset.Charset
import java.io.BufferedReader
import java.io.InputStreamReader

class MapLoader {

    companion object {

        fun loadSingleCharMap(resourceName: String): Array<Array<Cell>> {
            val encoding = Charset.defaultCharset()
            val inputStream = this::class.java.getResourceAsStream(resourceName)
            val reader = InputStreamReader(inputStream, encoding)
            val buffer = BufferedReader(reader)
            var singleChar: Char

            var terrainTypeArray = mutableListOf<TerrainType>()
            val terrainTypeMap = mutableListOf<List<TerrainType>>()

            while (true) {
                val readResult = buffer.read()
                if (readResult == -1) {
                    break
                }
                singleChar = readResult.toChar()
                if (singleChar == '\r') {
                    continue
                } else if (singleChar == '\n') {
                    terrainTypeMap.add(terrainTypeArray)
                    terrainTypeArray = mutableListOf()
                } else {
                    val terrainType = TerrainType.values()[singleChar.toString().toInt()]
                    terrainTypeArray.add(terrainType)
                }
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

        fun loadCSVMap(resourceName: String): Array<Array<Cell>> {
            val encoding = Charset.defaultCharset()
            val inputStream = this::class.java.getResourceAsStream(resourceName)
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
}