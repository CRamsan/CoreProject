
package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.game.actors.SpriteButton
import com.cramsan.stranded.lib.client.ui.game.widget.ReadyWidgetEventHandler
import ktx.scene2d.scene2d
import ktx.scene2d.stack

class ReadyButtonUIGdx(
    textureRegion: TextureRegion,
    width: Float,
    height: Float,
) : BaseUIComponent() {

    lateinit var eventHandler: ReadyWidgetEventHandler

    private val sprite = SpriteButton(
        textureRegion,
        width,
        height
    ) { eventHandler.onReadyButtonPressed() }

    override val widget = scene2d.stack {
        addActor(sprite)
    }
}
