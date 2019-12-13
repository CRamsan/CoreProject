package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Controls {

    lateinit var direction: Direction
    var inputBuffer = 0.0
    var handingInput = false

    fun update(delta: Float) {
        direction = Direction.NONE

        if (handingInput) {
            inputBuffer += delta
            if (inputBuffer > 0.2) {
                inputBuffer = 0.0
                handingInput = false
                return
            }
            return
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = Direction.LEFT
            inputBuffer = 0.0
            handingInput = true
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = Direction.RIGHT
            inputBuffer = 0.0
            handingInput = true
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = Direction.UP
            inputBuffer = 0.0
            handingInput = true
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = Direction.DOWN
            inputBuffer = 0.0
            handingInput = true
            return
        }
    }
}
