
package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.lib.client.ui.game.widget.ReadyWidgetEventHandler
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class ReadyButtonUIGdx : BaseUIComponent() {

    lateinit var eventHandler: ReadyWidgetEventHandler

    override val widget: Table = scene2d.table {
        pad(Theme.containerPadding)
        add(
            scene2d.textButton("Ready") {
                onClick {
                    eventHandler.onReadyButtonPressed()
                }
            }
        )
    }
}
