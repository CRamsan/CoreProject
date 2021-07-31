package com.cramsan.framework.sample.jvm_gui

import javax.swing.JFrame
import javax.swing.JPanel

class AWTRenderer() : JFrame() {

    init {
        setSize(400, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        isAlwaysOnTop = true
    }

    suspend fun startScene() {
        add(RendererCanvas())
    }

    internal inner class RendererCanvas() : JPanel()
}
