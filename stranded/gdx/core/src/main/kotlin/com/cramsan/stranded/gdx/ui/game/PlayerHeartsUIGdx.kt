package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
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

    private val instructionsText = scene2d.label("Tap to spend energy. 0 x Forage card")

    private val contentHolder: Table

    private val heartList: List<HeartToggleable> = (0 until 6).map {
        HeartToggleable(enabledTexture, disabledTexture, Theme.Scale.small, Theme.Scale.small)
    }

    private var enabled: Boolean = true

    override val hearts
        get() = heartList.count { it.toggleState }

    init {
        widget = scene2d.table {
            pad(Theme.containerPadding)
            heartList.forEachIndexed { _, heartToggleable ->
                add(heartToggleable).apply {
                    pad(Theme.containerSpacing)
                }
            }
            contentHolder = this
        }
        widget.addActor(instructionsText)
    }

    private fun recompose() {
        val hearts = player?.health ?: return
        heartList.forEachIndexed { index, heartToggleable ->
            heartToggleable.toggleState = index < hearts
        }

        instructionsText.setPosition(0f, widget.height)
    }

    override fun setEnabled(enabled: Boolean) {
        if (this.enabled == enabled) {
            return
        }

        this.enabled = enabled
        heartList.forEach { heartToggleable ->
            heartToggleable.enabled = enabled
        }

        val moveBy = if (enabled) {
            instructionsText.height + widget.height
        } else {
            instructionsText.height * -5
        }
        instructionsText.addAction(Actions.moveTo(0f, moveBy, Theme.Transtion.normal, Interpolation.fade))
    }

    override fun setHeartsContent(player: GamePlayer) {
        this.player = player
        recompose()
    }
}
