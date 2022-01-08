package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.common.Phase

interface CraftingUIWidget : UIComponent {

    fun setPhase(gamePhase: Phase)
}
