package com.cramsan.awslib.utils.pathfinding

interface Node {
    fun getX(): Int
    fun getY(): Int
    fun getCost(): Float
    fun getNeighbours(): List<Node>
}
