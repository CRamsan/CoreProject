package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/**
 * This class should be called once a frame to update the state. It is the ownership of this class to handle user inputs
 * and to ensure inputs are generated every X amount of time.
 */
class Controls {

    /**
     * Direction that the user input
     */
    lateinit var inputDirection: InputDirection

    fun update() {
        inputDirection = InputDirection.NONE

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            inputDirection = InputDirection.LEFT
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            inputDirection = InputDirection.RIGHT
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            inputDirection = InputDirection.UP
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            inputDirection = InputDirection.DOWN
            return
        }
    }
}
