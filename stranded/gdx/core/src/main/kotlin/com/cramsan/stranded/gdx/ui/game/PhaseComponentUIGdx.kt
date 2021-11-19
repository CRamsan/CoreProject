
package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.lib.client.ui.game.widget.PhaseComponentWidget
import com.cramsan.stranded.lib.game.models.common.Phase
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class PhaseComponentUIGdx : BaseUIComponent(), PhaseComponentWidget {

    var evenHandler: PhaseComponentAnimationHandler? = null

    private val label = scene2d.label("")
    private var phase: Phase = Phase.NIGHT

    private val changeTextAction = object : Action() {
        override fun act(delta: Float): Boolean {
            val text = when (phase) {
                Phase.FORAGING -> "Foraging"
                Phase.NIGHT_PREPARE -> "Preparing for the night"
                Phase.NIGHT -> "Night"
            }
            label.setText(text)
            return true
        }
    }

    override val widget: Table = scene2d.table {
        pad(Theme.containerPadding)
        add(label)
    }

    override fun setPhase(phase: Phase) {
        this.phase = phase

        widget.addAction(
            Actions.sequence(
                Actions.moveBy(0F, 100F, Theme.Transtion.fast, Interpolation.fade),
                changeTextAction,
                Actions.moveBy(0F, -100F, Theme.Transtion.fast, Interpolation.fade),
            )
        )

        evenHandler?.onPhaseChange(phase)
    }
}
