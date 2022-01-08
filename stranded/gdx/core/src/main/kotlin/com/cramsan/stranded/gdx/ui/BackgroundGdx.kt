package com.cramsan.stranded.gdx.ui

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.cramsan.stranded.lib.client.ui.widget.BackgroundWidget
import com.cramsan.stranded.lib.game.models.common.Phase
import ktx.graphics.copy

class BackgroundGdx(private val shapeRenderer: ShapeRenderer) : BackgroundWidget {

    private var colorActionBottom = ColorAction().apply {
        color = Theme.Color.background.copy()
    }
    private var colorActionTop = ColorAction().apply {
        color = Theme.Color.background.copy()
    }

    override fun setPhase(phase: Phase) {
        val startingColorBottom = colorActionBottom.color
        val startingColorTop = colorActionTop.color
        val targetColorBottom: Color
        val targetColorTop: Color
        when (phase) {
            Phase.FORAGING -> {
                targetColorBottom = Theme.Color.dayBottom
                targetColorTop = Theme.Color.dayTop
            }
            Phase.NIGHT_PREPARE -> {
                targetColorBottom = Theme.Color.sunsetBottom
                targetColorTop = Theme.Color.sunsetTop
            }
            Phase.NIGHT -> {
                targetColorBottom = Theme.Color.nightBottom
                targetColorTop = Theme.Color.nightTop
            }
        }
        colorActionBottom.apply {
            reset()
            color = startingColorBottom
            endColor = targetColorBottom
            duration = Theme.Transtion.normal
        }
        colorActionTop.apply {
            reset()
            color = startingColorTop
            endColor = targetColorTop
            duration = Theme.Transtion.normal
        }
    }

    override fun setVisible(isVisible: Boolean) = Unit

    fun act(delta: Float) {
        colorActionBottom.act(delta)
        colorActionTop.act(delta)
    }

    fun draw(width: Float, height: Float, camera: Camera) {
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.rect(
            0F,
            0F,
            width,
            height,
            colorActionBottom.color,
            colorActionBottom.color,
            colorActionTop.color,
            colorActionTop.color,
        )
        shapeRenderer.end()
    }
}
