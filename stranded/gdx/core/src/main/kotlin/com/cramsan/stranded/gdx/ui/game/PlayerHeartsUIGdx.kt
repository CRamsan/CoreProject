package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.actors.HeartToggleable
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHeartsWidget
import com.cramsan.stranded.lib.game.models.GamePlayer
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class PlayerHeartsUIGdx(
    enabledTexture: TextureRegion,
    disabledTexture: TextureRegion,
) : BaseUIComponent(), PlayerHeartsWidget {

    override val widget: Actor

    private var player: GamePlayer? = null

    private val contentHolder: Table

    private val heartList: List<HeartToggleable> = (0 until 6).map {
        HeartToggleable(enabledTexture, disabledTexture)
    }

    override val hearts
        get() = heartList.count { it.toggleState }

    init {
        widget = scene2d.table {
            pad(Theme.containerPadding)
            add(scene2d.label("Health")).apply {
                center()
                colspan(3)
            }
            row()
            heartList.forEachIndexed { index, heartToggleable ->
                add(heartToggleable).apply {
                    pad(Theme.containerSpacing)
                    if (index == 2) {
                        row()
                    }
                }
            }

            contentHolder = this
        }
    }

    private fun recompose() {
        val hearts = player?.health ?: return

        heartList.forEachIndexed { index, heartToggleable ->
            heartToggleable.toggleState = index < hearts
        }
    }

    override fun setEnabled(enabled: Boolean) {
        heartList.forEach { heartToggleable ->
            heartToggleable.enabled = enabled
        }
    }

    override fun setContent(player: GamePlayer) {
        this.player = player
        recompose()
    }
}
