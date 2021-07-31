package com.cramsan.framework.sample.jvm_gui

import kotlinx.coroutines.runBlocking
import java.awt.EventQueue

class AWTRunner {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            EventQueue.invokeLater(::createAndShowGUI)
        }

        private fun createAndShowGUI() {
            val renderer = AWTRenderer()
            runBlocking {
                renderer.startScene()
            }
        }
    }
}
