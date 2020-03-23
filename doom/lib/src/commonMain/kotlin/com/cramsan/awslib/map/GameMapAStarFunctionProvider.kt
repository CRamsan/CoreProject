package com.cramsan.awslib.map

import com.cramsan.awslib.utils.pathfinding.AStarFunctionProvider
import com.cramsan.awslib.utils.pathfinding.Node

class GameMapAStarFunctionProvider : AStarFunctionProvider {

    // For this game, the cost of traversing two adjacent node is based only on the terrain
    // type of the neighbor node
    override fun distanceBetween(current: Node, neighbor: Node): Float {
        return neighbor.getCost()
    }

    // The heuristic will rely on the cost of the node for the heuristic value
    override fun heuristicCostEstimate(neighbor: Node, goal: Node): Float {
        return neighbor.getCost()
    }
}
