package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Controls {
    var left: Boolean = false
    var right: Boolean = false
    var forward: Boolean = false
    var backward: Boolean = false

    init {
        backward = false
        forward = backward
        right = forward
        left = right
    }

    fun update() {
        backward = false
        forward = backward
        right = forward
        left = right

        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            left = true
        }
        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            right = true
        }
        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            forward = true
        }
        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            backward = true
        }

        if (com.badlogic.gdx.Gdx.input.isTouched) {
            if (com.badlogic.gdx.Gdx.input.y < com.badlogic.gdx.Gdx.graphics.height * 0.5) {
                forward = true
            } else if (com.badlogic.gdx.Gdx.input.x < com.badlogic.gdx.Gdx.graphics.width * 0.5) {
                left = true
            } else if (com.badlogic.gdx.Gdx.input.x > com.badlogic.gdx.Gdx.graphics.width * 0.5) {
                right = true
            }
        }
    }
}
