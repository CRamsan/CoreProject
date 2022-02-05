package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.actors.BaseCardUI
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandWidget
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Food
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.common.Weapon
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import ktx.scene2d.scene2d
import ktx.scene2d.table
import kotlin.random.Random

class HandUIGdx(
    private val cardTexture: TextureRegion,
) : BaseUIComponent(), PlayerHandWidget {

    lateinit var eventHandler: PlayerHandEventHandler

    override val widget: Table

    private var cardsUI: MutableList<BaseCardUI> = mutableListOf()
    private val contentHolder: Table

    init {
        widget = scene2d.table {
            contentHolder = this
        }
    }

    override fun setHandContent(player: GamePlayer) {
        val cards = player.belongings + player.scavengeResults + player.craftables
        cardsUI.forEach {
            contentHolder.removeActor(it.actor)
        }
        cards.forEach {
            addCard(it)
        }
    }

    override fun addCard(card: Card) {
        val baseCard = BaseCardUI(
            card,
            cardTexture,
            Theme.Scale.large,
            Theme.Scale.xlarge,
        ) {
            eventHandler.onCardSelected(card)
        }
        baseCard.actor.let {
            contentHolder.addActor(it)
            cardsUI.add(baseCard)

            val randomPosX = Random.nextDouble((-300).toDouble(), 300.toDouble())
            val randomPosY = Random.nextDouble((-50).toDouble(), 50.toDouble()).toFloat()
            it.setPosition(randomPosX.toFloat(), -300F + randomPosY)
            it.addAction(Actions.moveBy(0F, 500F, Theme.Transtion.fast, Interpolation.fade))
        }
    }

    override fun removeCard(card: Card) {
        val baseCard = cardsUI.find { it.card.id == card.id }!!

        baseCard.actor.addAction(
            Actions.sequence(
                Actions.moveBy(0F, -1000F, Theme.Transtion.fast, Interpolation.fade),
                Actions.run {
                    contentHolder.removeActor(baseCard.actor)
                    cardsUI.remove(baseCard)
                }
            )
        )
    }

    override fun setPhaseForHand(gamePhase: Phase) {
        val usableCards = mutableListOf<BaseCardUI>()
        val unusableCards = mutableListOf<BaseCardUI>()
        when (gamePhase) {
            Phase.FORAGING -> {
                cardsUI.forEach {
                    unusableCards.add(it)
                }
            }
            Phase.NIGHT_PREPARE -> {
                cardsUI.forEach {
                    if (it.card is Food || it.card is Resource) {
                        usableCards.add(it)
                    } else {
                        unusableCards.add(it)
                    }
                }
            }
            Phase.NIGHT -> {
                cardsUI.forEach {
                    if (it.card is Weapon) {
                        usableCards.add(it)
                    } else {
                        unusableCards.add(it)
                    }
                }
            }
        }
        usableCards.forEach {
            it.enable()
        }
        unusableCards.forEach {
            it.disable()
        }
    }
}
