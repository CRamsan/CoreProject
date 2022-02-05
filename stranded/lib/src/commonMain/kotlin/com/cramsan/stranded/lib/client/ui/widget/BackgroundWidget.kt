package com.cramsan.stranded.lib.client.ui.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.common.Phase

interface BackgroundWidget : UIComponent {
    fun setPhaseForBackground(phase: Phase)
}
