package com.cramsan.awslib.map

import com.cramsan.awslib.utils.pathfinding.Node
import com.cramsan.awslib.enums.TerrainType

abstract class Cell(var terrain: TerrainType) : Node, GridPositionableInterface {

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

    abstract fun onActionTaken()

    abstract fun blocksMovement(): Boolean
}