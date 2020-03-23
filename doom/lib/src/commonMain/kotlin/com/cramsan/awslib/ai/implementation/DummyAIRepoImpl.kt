package com.cramsan.awslib.ai.implementation

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.map.GameMapAStarFunctionProvider
import com.cramsan.awslib.map.GridPositionableInterface
import com.cramsan.awslib.utils.constants.GameEntityRange
import com.cramsan.awslib.utils.logging.Logger
import com.cramsan.awslib.utils.logging.Severity
import com.cramsan.awslib.utils.pathfinding.AStarAlgorithm
import kotlin.math.pow

/**
 * Simple implementation of [AIRepo]
 */
class DummyAIRepoImpl : AIRepo {

    override fun getNextTurnAction(entity: GameEntityInterface, entityManager: EntityManager, map: GameMap): TurnActionInterface {
        val target = getEntityTarget(entity, entityManager)

        if (target == null) {
            return TurnAction.NOOP
        }

        val functionProvider = GameMapAStarFunctionProvider()
        val startingCell = map.cellAt(entity.posX, entity.posY)
        val targetCell = map.cellAt(target.posX, target.posY)
        val path = AStarAlgorithm.findPath(startingCell, targetCell, getEntityRange(entity), functionProvider)

        if (path.size <= 1) {
            return TurnAction.NOOP
        }

        val nextNode = path[path.lastIndex - 1]
        val nextCell = map.cellAt(nextNode.getX(), nextNode.getY())
        if (path.size == 2) {
            return TurnAction(TurnActionType.ATTACK, cellToHeading(startingCell, nextCell))
        }

        return TurnAction(TurnActionType.MOVE, cellToHeading(startingCell, nextCell))
    }

    /**
     * Compare the x and y components of [positionable] and [otherPositionable] and return their diagonal distance as a Float.
     */
    private fun distance(positionable: GridPositionableInterface, otherPositionable: GridPositionableInterface): Float {
        val squarePower = 2
        val squareRoot = 0.5f
        return ((positionable.posY - otherPositionable.posY).toFloat().pow(squarePower) + (positionable.posX - otherPositionable.posX).toFloat().pow(squarePower)).pow(squareRoot)
    }

    /**
     * Get the Direction needed to face from [fromCell] towards [neighborCell]. Both cells need to be contiguous,
     * otherwise the behaviour is undefined.
     */
    private fun cellToHeading(fromCell: Cell, neighborCell: Cell): Direction {
        if (distance(fromCell, neighborCell) != 1f) {
            Logger.log(Severity.ERROR, "Cells are not neighbors")
            return Direction.KEEP
        }
        return when (neighborCell) {
            fromCell.northCell -> Direction.NORTH
            fromCell.southCell -> Direction.SOUTH
            fromCell.westCell -> Direction.WEST
            fromCell.eastCell -> Direction.EAST
            else -> Direction.KEEP
        }
    }

    /**
     * Retrieve the most suitable target for [entity]. Pass the [entityManager] to get the state of all the
     * other entities in the game.
     */
    private fun getEntityTarget(entity: GameEntityInterface, entityManager: EntityManager): GameEntityInterface? {
        var target: GameEntityInterface? = null
        entityManager.entitySet.forEach {
            val range = getEntityRange(entity)
            if (entity == it)
                return@forEach

            if (entity.type == it.type)
                return@forEach

            if (distance(entity, it) <= range) {
                return it
            }
        }
        return target
    }

    /**
     * Retrieve the vision range for the [entity].
     */
    private fun getEntityRange(entity: GameEntityInterface): Int {
        return when (entity.type) {
            EntityType.SCIENTIST -> GameEntityRange.SCIENTIST
            EntityType.PLAYER -> GameEntityRange.PLAYER
            EntityType.DOG -> GameEntityRange.DOG
        }
    }
}
