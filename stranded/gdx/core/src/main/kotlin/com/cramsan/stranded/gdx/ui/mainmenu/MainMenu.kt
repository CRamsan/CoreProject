
package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.controllers.DefaultMainMenuController
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup

class MainMenu : BaseUIComponent() {

    override val widget: Actor

    lateinit var controller: DefaultMainMenuController

    init {
        widget = scene2d {
            table {
                verticalGroup {
                    space(10F)
                    textButton("Create Game") {
                        onClick {
                            controller.openCreateLobbyMenu()
                        }
                    }
                    textButton("Join Game") {
                        onClick {
                            controller.openLobbyListMenu()
                        }
                    }
                    textButton("Change name") {
                        onClick {
                            controller.openPlayerNameMenu()
                        }
                    }
                    textButton("Quit") {
                        onClick {
                            controller.closeApplication()
                        }
                    }
                }
            }
        }
    }
}
