
package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenuEventHandler
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton
import ktx.scene2d.textField

class PlayerNameMenu : BaseUIComponent(), PlayerNameMenu {

    override val widget: Actor

    lateinit var eventHandler: PlayerNameMenuEventHandler

    var inputField: TextField

    init {
        widget = scene2d {
            table {
                pad(Theme.containerPadding)
                label("Enter your name:")
                inputField = textField { }
                textButton("OK") {
                    onClick {
                        eventHandler.onPlayerNameConfirmed(inputField.text)
                        inputField.text = ""
                    }
                }
                row()
                textButton("Debug Screen") {
                    onClick {
                        eventHandler.onDebugScreenSelected()
                    }
                }
            }
        }
    }

    override fun setPlayerName(playerName: String) {
        inputField.text = playerName
    }
}
