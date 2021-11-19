
package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.CreateLobbyMenuEventHandler
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton
import ktx.scene2d.textField

class CreateLobbyMenu : BaseUIComponent() {

    override val widget: Actor

    lateinit var eventHandler: CreateLobbyMenuEventHandler

    init {
        widget = scene2d {
            table {
                label("Lobby name:")
                val inputField = textField {
                }
                row()
                textButton("Back") {
                    onClick {
                        eventHandler.onReturnToMainMenuSelected()
                    }
                }
                textButton("OK") {
                    onClick {
                        eventHandler.onCreateLobbySelected(inputField.text)
                        inputField.text = ""
                    }
                }
            }
        }
    }
}
