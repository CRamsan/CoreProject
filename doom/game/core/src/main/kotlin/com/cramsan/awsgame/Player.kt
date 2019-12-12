package com.cramsan.awsgame

import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.enums.Direction

class Player(val player: Player) {
    var paces: Double = 0.toDouble()
    var direction = Direction.NORTH

    var weapon: com.badlogic.gdx.graphics.Texture

    init {
        this.paces = 0.0
        this.weapon = com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal("knife_hand.png"))
    }

    fun rotate(direction: Direction) {
        this.direction = direction
    }

    fun angleFromDirection(direction: Direction): Double {
        return when(direction) {
            Direction.NORTH -> 0.0
            Direction.SOUTH -> 180.0
            Direction.WEST -> 270.0
            Direction.EAST -> 90.0
            Direction.KEEP -> 0.0
        }
    }

    /*
    fun walk(direction: Direction) {
        var action = TurnAction(TurnActionType.MOVE, direction)
        scene.runTurn(action)
        this.paces++
    }
    */

    /*
    fun update(controls: Controls, map: Map, seconds: Double) {
    }
    */

    fun toPoint(): Point {
        return Point(this.player.posX.toDouble(), this.player.posY.toDouble())
    }
}
