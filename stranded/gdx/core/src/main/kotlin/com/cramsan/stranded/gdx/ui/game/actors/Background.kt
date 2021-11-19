package com.cramsan.stranded.gdx.ui.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor

class Background(
    private val shapeRenderer: ShapeRenderer,
) : Actor() {

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.end()

        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.transformMatrix = batch.transformMatrix
        shapeRenderer.translate(x, y, 0F)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        shapeRenderer.rect(0F, 0F, width, height)
        shapeRenderer.end()

        batch.begin()
    }
}
