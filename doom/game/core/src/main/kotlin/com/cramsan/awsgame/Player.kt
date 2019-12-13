package com.cramsan.awsgame

import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.enums.Direction

class Player(val player: Player) {
    var paces: Double = 0.toDouble()

    var weapon: com.badlogic.gdx.graphics.Texture

    init {
        this.paces = 0.0
        this.weapon = com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal("knife_hand.png"))
    }

    fun getGameDirectionFromInput(inputDirection: com.cramsan.awsgame.Direction): Direction {
        return when(inputDirection) {
            com.cramsan.awsgame.Direction.UP -> direction()
            com.cramsan.awsgame.Direction.DOWN -> {
                when(direction()) {
                    Direction.NORTH -> Direction.SOUTH
                    Direction.SOUTH -> Direction.NORTH
                    Direction.WEST -> Direction.EAST
                    Direction.EAST -> Direction.WEST
                    Direction.KEEP -> Direction.KEEP
                }
            }
            com.cramsan.awsgame.Direction.LEFT -> {
                this.player.heading = when(direction()) {
                    Direction.NORTH -> Direction.WEST
                    Direction.SOUTH -> Direction.EAST
                    Direction.WEST -> Direction.SOUTH
                    Direction.EAST -> Direction.NORTH
                    Direction.KEEP -> Direction.KEEP
                }
                Direction.KEEP
            }
            com.cramsan.awsgame.Direction.RIGHT -> {
                this.player.heading = when(direction()) {
                    Direction.NORTH -> Direction.EAST
                    Direction.SOUTH -> Direction.WEST
                    Direction.WEST -> Direction.NORTH
                    Direction.EAST -> Direction.SOUTH
                    Direction.KEEP -> Direction.KEEP
                }
                Direction.KEEP
            }
            com.cramsan.awsgame.Direction.NONE -> Direction.KEEP
        }
    }

    fun angleFromDirection(): Double {
        return when(direction()) {
            Direction.NORTH -> Math.PI * -0.5
            Direction.SOUTH -> Math.PI * 0.5
            Direction.WEST -> Math.PI * 1.0
            Direction.EAST -> Math.PI * 0.0
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
    fun direction(): Direction {
        return this.player.heading
    }

    fun toPoint(): Point {
        return Point(this.player.posX.toDouble() + 0.5, this.player.posY.toDouble() + 0.5)
    }
}
