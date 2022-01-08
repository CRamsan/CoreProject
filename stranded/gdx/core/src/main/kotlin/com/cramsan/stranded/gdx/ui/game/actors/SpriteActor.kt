package com.cramsan.stranded.gdx.ui.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

open class SpriteActor(
    protected val textureRegion: TextureRegion,
) : Actor() {

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }
}
