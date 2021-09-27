package com.cramsan.awslib.map

import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.utils.pathfinding.Node

sealed class Cell(var terrain: TerrainType) : Node, GridPositionableInterface {

    override var posX: Int = -1
    override var posY: Int = -1
    var northCell: Cell? = null
    var southCell: Cell? = null
    var eastCell: Cell? = null
    var westCell: Cell? = null

    override fun getX(): Int {
        return posX
    }

    override fun getY(): Int {
        return posY
    }

    override fun getCost(): Float {
        if (terrain == TerrainType.WALL) {
            return Float.POSITIVE_INFINITY
        }
        return 1f
    }

    override fun getNeighbours(): List<Node> {
        return listOfNotNull(northCell, southCell, eastCell, westCell)
    }

    // abstract fun onActionTaken()

    abstract fun blocksMovement(): Boolean

    override fun toString(): String {
        return "x:$posX, y:$posY, terrain:$terrain"
    }
}

class DoorCell(var opened: Boolean = false) : Cell(TerrainType.DOOR) {
    override fun blocksMovement(): Boolean {
        return opened.not()
    }
}

class EndCell : Cell(TerrainType.END) {
    override fun blocksMovement(): Boolean {
        return false
    }
}

class OpenCell : Cell(TerrainType.OPEN) {
    override fun blocksMovement(): Boolean {
        return false
    }
}

class WallCell : Cell(TerrainType.WALL) {
    override fun blocksMovement(): Boolean {
        return true
    }
}
