package com.cramsan.awslib.map

class GameMap(mapArray: Array<Array<Cell>>) {

    val map: Array<Array<Cell>>
    val width: Int
    val height: Int

    init {
        map = linkCells(mapArray)
        width = map.size
        height = map.first().size
    }

    fun isBlocked(posX: Int, posY: Int): Boolean {
        if (posX >= map.size || posY >= map.first().size || posX < 0 || posY < 0)
            return true
        return map[posX][posY].blocksMovement()
    }

    fun cellAt(posX: Int, posY: Int): Cell {
        return map[posX][posY]
    }

    private fun linkCells(map: Array<Array<Cell>>): Array<Array<Cell>> {
        for (column in map.withIndex()) {
            for (row in column.value.withIndex()) {
                val cell = row.value
                cell.posX = column.index
                cell.posY = row.index
                if (row.index - 1 >= 0)
                    cell.northCell = map[column.index][row.index - 1]

                if (row.index + 1 < map.first().size)
                    cell.southCell = map[column.index][row.index + 1]

                if (column.index + 1 < map.size)
                    cell.eastCell = map[column.index + 1][row.index]

                if (column.index - 1 >= 0)
                    cell.westCell = map[column.index - 1][row.index]
            }
        }
        return map
    }
}
