package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.cramsan.awsgame.GameInput

/**
 * This class should be called once a frame to update the state. It is the ownership of this class to handle user inputs
 * and to ensure inputs are generated every X amount of time.
 */
class Controls {

    /**
     * Direction that the user input
     */
    lateinit var input: GameInput

    fun update() {
        input = GameInput.NONE

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            input = GameInput.LEFT
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            input = GameInput.RIGHT
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            input = GameInput.UP
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            input = GameInput.DOWN
            return
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            input = GameInput.ACTION
            return
        }
    }
}
