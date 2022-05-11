package com.cramsan.awsgame.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.cramsan.awsgame.Direction
import com.cramsan.awsgame.GameInput
import com.cramsan.awslib.entity.implementation.Player
import kotlin.math.PI

class Player(private val player: Player) {

    private val transitionTime = 0.5F
    private var transitionCounter = 0.0F

    private var isTurning = false
    private var startingAngle = 0.0
    private var turnDirection = 0.0

    private var isMoving = false
    private var startingX = 0.0
    private var startingY = 0.0
    private var smoothAngle = 0.0
    private lateinit var pointLocation: Point

    var isAttacking = false

    private var inputBuffer: Float = 0.0F
    private var blockedInput: Boolean = false

    var paces: Double = 0.0
    var weapon: Texture = Texture(Gdx.files.internal("knife_hand.png"))
    var move: com.cramsan.awslib.enums.Direction? = null

    fun update(delta: Float, gameInput: GameInput) {

        move = null
        isAttacking = false

        if (blockedInput) {
            inputBuffer += delta
            paces += delta
            transitionCounter += delta

            if (isTurning) {
                var completion = transitionCounter / transitionTime
                if (completion > 1) {
                    completion = 1F
                }
                val newAngle = startingAngle + ((PI / 2) * completion * turnDirection)
                smoothAngle = newAngle
            }

            if (isMoving) {
                var completion = transitionCounter / transitionTime
                if (completion > 1) {
                    completion = 1F
                }
                val x = this.player.posX.toDouble() + 0.5
                val y = this.player.posY.toDouble() + 0.5
                val dX = completion * (x - startingX)
                val dY = completion * (y - startingY)
                pointLocation = Point(startingX + dX, startingY + dY)
            }

            if (inputBuffer > transitionTime) {
                inputBuffer = 0.0F
                blockedInput = false
                isTurning = false
                isMoving = false
                return
            }
            return
        }

        smoothAngle = direction().angle()
        pointLocation = Point(
            this.player.posX.toDouble() + 0.5,
            this.player.posY.toDouble() + 0.5,
        )

        if (gameInput == GameInput.NONE) {
            return
        }

        when (gameInput) {
            GameInput.LEFT -> {
                startingAngle = direction().angle()
                this.player.heading = direction().turnLeft().direction
                isTurning = true
                transitionCounter = 0.0F
                turnDirection = -1.0
            }
            GameInput.RIGHT -> {
                startingAngle = direction().angle()
                this.player.heading = direction().turnRight().direction
                isTurning = true
                transitionCounter = 0.0F
                turnDirection = 1.0
            }
            GameInput.UP -> {
                move = direction().direction
                isMoving = true
                val point = toPoint()
                startingX = point.x
                startingY = point.y
                transitionCounter = 0.0F
            }
            GameInput.DOWN -> {
                move = direction().turnAround().direction
                isMoving = true
                val point = toPoint()
                startingX = point.x
                startingY = point.y
                transitionCounter = 0.0F
            }
            GameInput.ACTION -> {
                isMoving = true
                isAttacking = true
                val point = toPoint()
                startingX = point.x
                startingY = point.y
                transitionCounter = 0.0F
            }
            else -> {
                TODO()
            }
        }
        blockedInput = true
    }

    fun angle(): Double {
        return smoothAngle
    }

    fun direction(): Direction {
        return Direction.fromInternalDirection(this.player.heading)
    }

    fun toPoint(): Point {
        return pointLocation
    }
}
