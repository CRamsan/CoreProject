package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.widget.LobbyListWidget
import com.cramsan.stranded.lib.repository.Lobby
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class LobbyListWidgetGdx(
    private val onClick: (String) -> Unit
) : BaseUIComponent(), LobbyListWidget {

    override val widget: Actor

    private var lobbyList: List<Lobby> = emptyList()

    private val contentHolder: Table

    init {
        widget = scene2d {
            table {
                pad(10f)
                contentHolder = this
            }
        }
    }

    private fun recompose() {
        contentHolder.clearChildren()

        lobbyList.forEach { lobby ->
            contentHolder.add(
                scene2d {
                    textButton(lobby.name) {
                        onClick {
                            onClick(lobby.id)
                        }
                    }
                }
            )
        }
        contentHolder.invalidate()
    }

    override fun setLobbyList(lobbyList: List<Lobby>) {
        this.lobbyList = lobbyList
        recompose()
    }
}
