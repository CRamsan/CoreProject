package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.actors.SpriteButton
import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingWidgetEventHandler
import ktx.scene2d.scene2d
import ktx.scene2d.table

class CraftingUIGdx(
    basketTexture: TextureRegion,
    shelterTexture: TextureRegion,
    spearTexture: TextureRegion,
) : BaseUIComponent(), UIComponent {

    override val widget: Actor

    lateinit var eventHandler: CraftingWidgetEventHandler

    private val contentHolder: Table

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
            row().space(Theme.containerSpacing)
            add(basketButton)
            row().space(Theme.containerSpacing)
            add(shelterButton)
            contentHolder = this
        }
    }

    override fun setVisible(isVisible: Boolean) {
        widget.isVisible = isVisible
    }
}
