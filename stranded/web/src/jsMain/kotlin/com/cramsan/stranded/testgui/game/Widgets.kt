package com.cramsan.stranded.testgui.game

import androidx.compose.runtime.Composable
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Suppress("FunctionNaming")
@Composable
fun Header(
    name: String,
    health: Int,
) {
    P {
        val content = if (health == 0) {
            ""
        } else {
            "❤️x$health"
        }
        Span {
            Text(name)
        }
        Span {
            Text(content)
        }
    }
}

@Suppress("FunctionNaming")
@Composable
fun Phase(
    phase: Phase,
) {
    P {
        Text(phase.name)
    }
}

@Suppress("FunctionNaming")
@Composable
fun Day(
    day: Int,
) {
    P {
        Text("Day: $day")
    }
}
