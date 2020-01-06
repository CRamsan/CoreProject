package com.cramsan.awsgame.screen

abstract class GameScreen() : BaseScreen() {

    override fun performCustomUpdate(delta: Float) {}

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    abstract override fun levelId(): Int
}