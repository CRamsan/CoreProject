package com.cramsan.stranded.gdx.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.cramsan.stranded.gdx.StrandedGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setWindowedMode(1080, 1920)
    }
    Lwjgl3Application(StrandedGame(), config)
}
