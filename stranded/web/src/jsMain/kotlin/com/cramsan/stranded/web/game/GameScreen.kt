package com.cramsan.stranded.web.game

import androidx.compose.runtime.Composable
import com.cramsan.stranded.lib.game.models.common.Phase
import org.jetbrains.compose.web.dom.Div

@Suppress("LongMethod", "FunctionNaming", "UNUSED_PARAMETER")
@Composable
fun GameScreen(
    name: String,
    health: Int,
    phase: Phase,
    day: Int,
    viewModel: GameViewModel? = null,
) {
    Div {
        HeaderView(name, health)

        PhaseView(phase)

        DayView(day)
    }
}
