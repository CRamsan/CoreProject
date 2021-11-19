
package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenuEventHandler
import com.cramsan.stranded.lib.repository.Lobby
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class LobbyListMenuGdx : BaseUIComponent(), LobbyListMenu {

    override val widget: Actor

    lateinit var eventHandler: LobbyListMenuEventHandler

    private var lobbyListUI: LobbyListWidgetGdx = LobbyListWidgetGdx { lobbyId ->
        eventHandler.onLobbySelected(lobbyId)
    }

    init {

        widget = scene2d {
            table {
                label("Available lobbies:")
                row()
                add(lobbyListUI.widget)
                row()
                textButton("Back") {
                    onClick {
                        eventHandler.onReturnToMainMenuSelected()
                    }
                }
            }
        }
    }

    override fun setLobbyList(lobbyList: List<Lobby>) {
        lobbyListUI.setLobbyList(lobbyList)
    }
}
