package com.cramsan.stranded.gdx.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.widget.LobbyPlayerListWidget
import com.cramsan.stranded.lib.repository.Player
import ktx.scene2d.horizontalGroup
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class LobbyPlayerListUIGdx : BaseUIComponent(), LobbyPlayerListWidget {

    override val widget: Actor

    private val playerList: MutableList<Player> = mutableListOf()
    private val contentHolder: Table

    init {
        widget = scene2d.table {
            pad(10F)
            contentHolder = this
        }
    }

    private fun recompose() {
        contentHolder.clearChildren()
        playerList.forEach { player ->
            recomposePlayer(player)
        }
        contentHolder.invalidate()
    }

    private fun recomposePlayer(player: Player) {
        val readyText = when (player.readyToStart) {
            true -> "READY"
            false -> "NOT READY"
        }
        contentHolder.add(
            scene2d.horizontalGroup {
                space(5F)
                label(player.name)
                label(readyText)
            }
        )
    }

    override fun updatePlayer(player: Player) {
        playerList.find { it.id == player.id }!!.readyToStart = player.readyToStart
        recompose()
    }

    override fun addPlayer(player: Player) {
        playerList.add(player)
        recompose()
    }

    override fun removePlayer(player: Player) {
        playerList.removeIf { it.id == player.id }
        recompose()
    }

    override fun setPlayerList(playerList: List<Player>) {
        this.playerList.clear()
        this.playerList.addAll(playerList)
        recompose()
    }
}
