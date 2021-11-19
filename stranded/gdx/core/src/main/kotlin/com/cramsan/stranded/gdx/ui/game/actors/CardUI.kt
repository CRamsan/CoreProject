package com.cramsan.stranded.gdx.ui.game.actors

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class CardUI(
    val card: Card,
    textureRegion: TextureRegion,
    baseWidth: Float = 128F,
    baseHeight: Float = 280F,
    private val onClick: () -> Unit
) {

    val actor: Actor

    private var initialTouchX = 0F
    private var initialTouchY = 0F
    private var wasDragged = false

    init {
        val baseCard = BaseCard(textureRegion)
        val label = when (card) {
            is Resource -> card.resourceType.name
            else -> card.javaClass.simpleName
        }
        actor = scene2d.stack {
            add(baseCard)
            add(
                scene2d.table {
                    label(label)
                }
            )
        }
        actor.isTransform = true
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.addAction(Actions.scaleTo(1.03F, 1.03F, Theme.Transtion.xxfast, Interpolation.linear))
                initialTouchX = x
                initialTouchY = y
                wasDragged = false
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                actor.addAction(Actions.scaleTo(1F, 1F, Theme.Transtion.xxfast, Interpolation.linear))
                initialTouchX = 0F
                initialTouchY = 0F
                super.touchUp(event, x, y, pointer, button)
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                val dx = x - initialTouchX
                val dy = y - initialTouchY
                wasDragged = true
                actor.moveBy(dx, dy)
            }

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)

                if (!wasDragged) {
                    onClick()
                }
            }
        })
        actor.width = baseWidth
        actor.height = baseHeight
        actor.setOrigin(Align.center)
    }
}
