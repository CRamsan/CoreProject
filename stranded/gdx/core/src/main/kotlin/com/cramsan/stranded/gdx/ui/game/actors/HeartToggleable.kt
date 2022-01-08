package com.cramsan.stranded.gdx.ui.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.cramsan.stranded.gdx.ui.Theme

class HeartToggleable(
    private val enabledTextureRegion: TextureRegion,
    private val disabledTextureRegion: TextureRegion,
    baseWidth: Float,
    baseHeight: Float,
    initialToggleState: Boolean = false,
) : Actor() {

    private var currentTextureRegion: TextureRegion = enabledTextureRegion

    var toggleState: Boolean = initialToggleState
        set(value) {
            currentTextureRegion = if (value) {
                enabledTextureRegion
            } else {
                disabledTextureRegion
            }
            field = value
        }

    var enabled: Boolean = false

    init {
        addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (enabled) {
                    addAction(Actions.scaleTo(1.3F, 1.3F, Theme.Transtion.xxfast, Interpolation.linear))
                }

                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                if (enabled) {
                    addAction(Actions.scaleTo(1F, 1F, Theme.Transtion.xxfast, Interpolation.linear))
                }

                super.touchUp(event, x, y, pointer, button)
            }
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (enabled) {
                    toggleState = !toggleState
                }
            }
        })
        width = baseWidth
        height = baseHeight
        setOrigin(Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(currentTextureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }
}
