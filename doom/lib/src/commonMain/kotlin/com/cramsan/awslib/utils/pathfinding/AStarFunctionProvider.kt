package com.cramsan.awslib.utils.pathfinding

interface AStarFunctionProvider {

    fun distanceBetween(current: Node, neighbor: Node): Float

    fun heuristicCostEstimate(neighbor: Node, goal: Node): Float
}
