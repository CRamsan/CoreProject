package com.cramsan.awsgame.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.cramsan.awsgame.MyGdxGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(450, 800)
    Lwjgl3Application(MyGdxGame(), config)
}
