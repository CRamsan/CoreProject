
package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.game.PauseMenuEventHandler
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class PauseMenu : BaseUIComponent() {

    override val widget: Actor

    lateinit var eventHandler: PauseMenuEventHandler

    init {
        widget = scene2d {
            table {
                textButton("Quit") {
                    onClick {
                        eventHandler.onExitGameSelected()
                    }
                }
                this.isVisible = false
            }
        }
    }
}
