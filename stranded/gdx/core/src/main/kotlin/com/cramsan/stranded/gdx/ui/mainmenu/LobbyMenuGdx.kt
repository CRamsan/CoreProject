
package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenuEventHandler
import com.cramsan.stranded.lib.repository.Player
import ktx.actors.onClick
import ktx.scene2d.checkBox
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class LobbyMenuGdx(
    private val mainPlayerId: String,
    private val lobbyPlayerList: LobbyPlayerListUIGdx,
) : BaseUIComponent(), LobbyMenu {

    override val widget: Actor

    lateinit var eventHandler: LobbyMenuEventHandler

    private var isPlayerReady = false
    private val checkBox: CheckBox

    init {
        widget = scene2d {
            table {
                label("Players:")
                row()
                add(lobbyPlayerList.widget)
                row()
                textButton("Back") {
                    onClick {
                        eventHandler.onLeaveLobbySelected()
                    }
                }
                checkBox("Ready") {
                    checkBox = this
                    onClick {
                        eventHandler.onReadySelected(isChecked)
                    }
                }
                textButton("Start") {
                    onClick {
                        eventHandler.onStartGameSelected()
                    }
                }
            }
        }
    }

    override fun updatePlayer(player: Player) {
        if (player.id == mainPlayerId) {
            isPlayerReady = player.readyToStart
            checkBox.isChecked = isPlayerReady
        }
        lobbyPlayerList.updatePlayer(player)
    }

    override fun setPlayerList(playerList: List<Player>) {
        lobbyPlayerList.setPlayerList(playerList)
    }

    override fun addPlayer(player: Player) {
        lobbyPlayerList.addPlayer(player)
    }

    override fun removePlayer(player: Player) {
        lobbyPlayerList.removePlayer(player)
    }
}
