package com.cramsan.stranded.gdx.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.cramsan.stranded.lib.client.UIComponent

abstract class BaseUIComponent : UIComponent {

    abstract val widget: Actor

    override fun setVisible(isVisible: Boolean) {
        widget.isVisible = isVisible
    }
}
