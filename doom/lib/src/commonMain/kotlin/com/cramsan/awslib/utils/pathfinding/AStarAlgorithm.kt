package com.cramsan.awslib.utils.pathfinding

import com.cramsan.awslib.platform.KotlinPriorityQueue

// Reference:
// https://en.wikipedia.org/wiki/A*_search_algorithm

class AStarAlgorithm {

    companion object {

        private fun reconstructPath(cameFrom: Map<Node, Node>, lastNode: Node): List<Node> {
            val totalPath = mutableListOf(lastNode)
            var current: Node = lastNode
            while (current in cameFrom.keys) {
                current = cameFrom.getValue(current)
                totalPath.add(current)
            }
            return totalPath
        }

        fun findPath(start: Node, goal: Node, maxCost: Int, functionProvider: AStarFunctionProvider): List<Node> {
            // Set of nodes already evaluated
            val closedSet = mutableSetOf<Node>()

            // For each node, the total cost of getting from the start node to the goal
            // by passing by that node. That value is partly known, partly heuristic.
            val fScore = mutableMapOf<Node, Float>()

            // The set of currently discovered nodes that are not evaluated yet.
            // Initially, only the start node is known.
            val openSet = KotlinPriorityQueue(NodeComparator(fScore))
            openSet.add(start)

            // For each node, which node it can most efficiently be reached from.
            // If a node can be reached from many nodes, cameFrom will eventually contain the
            // most efficient previous step.
            val cameFrom = mutableMapOf<Node, Node>()

            // For each node, the cost of getting from the start node to that node.
            val gScore = mutableMapOf<Node, Float>()

            // The cost of going from start to start is zero.
            gScore[start] = 0f

            // For the first node, that value is completely heuristic.
            fScore[start] = functionProvider.heuristicCostEstimate(start, goal)

            while (openSet.size() > 0) {

                val current = openSet.remove()

                if (current == goal)
                    return reconstructPath(cameFrom, current)

                openSet.remove(current)
                closedSet.add(current)

                for (neighbor in current.getNeighbours()) {
                    if (neighbor in closedSet)
                        continue
                    // Ignore the neighbor which is already evaluated.

                    // The distance from start to a neighbor
                    val tentativeGScore = gScore.getValue(current).plus(functionProvider.distanceBetween(current, neighbor))
                    if (tentativeGScore > maxCost) {
                        continue
                    }

                    var shouldAdd = false
                    if (neighbor !in openSet) { // Discover a new node
                        fScore[neighbor] = Float.POSITIVE_INFINITY
                        shouldAdd = true
                    } else if (tentativeGScore >= gScore.getValue(current)) {
                        continue
                    }

                    // This path is the best until now. Record it!
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    val calc = gScore.getValue(current) + functionProvider.heuristicCostEstimate(neighbor, goal)
                    fScore[neighbor] = calc

                    // The set will be ordered based on the values of fScore, therefore we need to update
                    // fScore before we can add the node to the ordered set.
                    if (shouldAdd) {
                        openSet.add(neighbor)
                    }
                }
            }
            return emptyList()
        }

        private class NodeComparator(val fScore: Map<Node, Float>) : Comparator<Node> {
            override fun compare(a: Node, b: Node): Int {
                val aScore: Float = fScore.getValue(a)
                val bScore: Float = fScore.getValue(b)
                return aScore.compareTo(bScore)
            }
        }
    }
}