package com.cramsan.stranded.gdx.ui.game

import com.cramsan.stranded.lib.game.models.common.Phase

interface PhaseComponentAnimationHandler {

    fun onPhaseChange(phase: Phase)
}
