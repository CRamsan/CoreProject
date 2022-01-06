package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.actors.BaseCardUI
import com.cramsan.stranded.lib.client.ui.game.widget.NightCardWidget
import com.cramsan.stranded.lib.game.models.common.Card
import ktx.scene2d.scene2d
import ktx.scene2d.table

class NightCardUIGdx(
    private val cardTexture: TextureRegion,
) : BaseUIComponent(), NightCardWidget {

    override val widget: Table

    private var cardsUI: BaseCardUI? = null
    private val contentHolder: Table

    init {
        widget = scene2d.table {
            contentHolder = this
        }
    }

    override fun displayCard(card: Card) {
        val baseCard = BaseCardUI(
            card,
            cardTexture,
            Theme.Scale.xlarge,
            Theme.Scale.xxlarge,
        ) {

        }
        baseCard.actor.let {
            contentHolder.addActor(it)
            cardsUI = baseCard

            it.setPosition(it.stage.width, it.stage.height / 2 - it.height / 2)
            it.addAction(Actions.moveBy((it.stage.width / -2) - (it.width / 2), 0F, Theme.Transtion.fast, Interpolation.fade))
        }
    }

    override fun hideCard() {
        cardsUI?.let {
            it.actor.addAction(
                Actions.sequence(
                    Actions.moveBy(it.actor.stage.width * -2, 0F, Theme.Transtion.fast, Interpolation.fade),
                    Actions.run {
                        contentHolder.removeActor(it.actor)
                    }
                )
            )
        }
    }
}
