package com.cramsan.awslib.ai.implementation

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.entity.CharacterInterface
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
import com.cramsan.awslib.utils.pathfinding.AStarAlgorithm
import com.cramsan.framework.logging.EventLoggerInterface
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.math.pow

/**
 * Simple implementation of [AIRepo]
 */
class DummyAIRepoImpl(override val di: DI) : AIRepo, DIAware {

    private val log: EventLoggerInterface by instance()
    private val tag = "DummyAIRepoImpl"

    override fun getNextTurnAction(character: CharacterInterface, entityManager: EntityManager, map: GameMap): TurnActionInterface {
        val target = getCharacterTarget(character, entityManager)

        if (target == null) {
            return TurnAction.NOOP
        }

        val functionProvider = GameMapAStarFunctionProvider()
        val startingCell = map.cellAt(character.posX, character.posY)
        val targetCell = map.cellAt(target.posX, target.posY)
        val path = AStarAlgorithm.findPath(startingCell, targetCell, getEntityRange(character), functionProvider)

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
            log.e(tag, "Cells are not neighbors")
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
     * Retrieve the most suitable target for [character]. Pass the [entityManager] to get the state of all the
     * other entities in the game.
     */
    private fun getCharacterTarget(character: CharacterInterface, entityManager: EntityManager): CharacterInterface? {
        var target: CharacterInterface? = null
        entityManager.characterSet.forEach {
            val range = getEntityRange(character)
            if (character == it)
                return@forEach

            if (character.type == it.type)
                return@forEach

            if (distance(character, it) <= range) {
                return it
            }
        }
        return target
    }

    /**
     * Retrieve the vision range for the [character].
     */
    private fun getEntityRange(character: CharacterInterface): Int {
        return when (character.type) {
            EntityType.SCIENTIST -> GameEntityRange.SCIENTIST
            EntityType.PLAYER -> GameEntityRange.PLAYER
            EntityType.DOG -> GameEntityRange.DOG
        }
    }
}
