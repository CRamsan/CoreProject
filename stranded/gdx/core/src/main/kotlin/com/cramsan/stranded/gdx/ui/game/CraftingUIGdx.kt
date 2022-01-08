package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.actors.SpriteButton
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingUIWidget
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingWidgetEventHandler
import com.cramsan.stranded.lib.game.models.common.Phase
import ktx.scene2d.scene2d
import ktx.scene2d.table

class CraftingUIGdx(
    basketTexture: TextureRegion,
    shelterTexture: TextureRegion,
    spearTexture: TextureRegion,
) : BaseUIComponent(), CraftingUIWidget {

    override val widget: Actor

    lateinit var eventHandler: CraftingWidgetEventHandler

    private val contentHolder: Table

    private var enabled: Boolean = true

    init {
        val spearTextureButton = SpriteButton(spearTexture) {
            eventHandler.createSpear()
        }
        val basketButton = SpriteButton(basketTexture) {
            eventHandler.createBasket()
        }
        val shelterButton = SpriteButton(shelterTexture) {
            eventHandler.createShelter()
        }
        widget = scene2d.table {
            pad(Theme.containerPadding)
            add(spearTextureButton)
            add(basketButton)
            add(shelterButton)
            contentHolder = this
        }
    }

    override fun setPhase(gamePhase: Phase) {
        val newState = gamePhase == Phase.NIGHT_PREPARE

        if (enabled == newState) {
            return
        }

        enabled = newState
        if (enabled) {
            widget.addAction(Actions.moveTo(0f, Theme.Scale.large, Theme.Transtion.normal, Interpolation.fade))
        } else {
            widget.addAction(Actions.moveTo(0f, -100f, Theme.Transtion.normal, Interpolation.fade))
        }
    }

    override fun setVisible(isVisible: Boolean) {
        widget.isVisible = isVisible
    }
}
