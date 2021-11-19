package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerListWidget
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.repository.Player
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class PlayerListUIGdx : BaseUIComponent(), PlayerListWidget {

    override val widget: Actor

    private var playerList: List<GamePlayer> = emptyList()
    private val contentHolder: Table

    init {
        widget = scene2d.table {
            pad(Theme.containerPadding)
            label("Players")
            row()
            table {
                pad(Theme.containerPadding)
                contentHolder = this
            }
        }
    }

    private fun recompose() {
        contentHolder.clearChildren()
        playerList.forEach { player ->
            contentHolder.add(
                scene2d.label(player.nane) {
                    setEllipsis(true)
                }
            ).apply {
                width(120F)
                pad(Theme.containerSpacing)
            }
            contentHolder.add(scene2d.label("Health: ${player.health}")).apply {
                width(75F)
            }
            contentHolder.row()
        }
        contentHolder.invalidate()
    }

    override fun setPlayerList(playerList: List<GamePlayer>) {
        this.playerList = playerList
        recompose()
    }

    override fun updatePlayer(player: Player) {
    }
}
