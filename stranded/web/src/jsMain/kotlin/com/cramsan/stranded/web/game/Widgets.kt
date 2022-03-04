package com.cramsan.stranded.web.game

import androidx.compose.runtime.Composable
import com.cramsan.stranded.lib.game.models.common.Phase
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Suppress("FunctionNaming")
@Composable
fun HeaderView(
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
fun PhaseView(
    phase: Phase,
) {
    P {
        Text(phase.name)
    }
}

@Suppress("FunctionNaming")
@Composable
fun DayView(
    day: Int,
) {
    P {
        Text("Day: $day")
    }
}

/*
@Suppress("FunctionNaming")
@Composable
fun MessageView(
    message: Message,
) {
    P {
        message.content.forEach {
            Span {
                when (it) {
                    is MessageElement.Action -> ActionView(it)
                    is MessageElement.Craftable -> CraftableView(it)
                    is MessageElement.Food -> FoodView(it)
                    is MessageElement.Normal -> NormalView(it)
                    is MessageElement.Weapon -> WeaponView(it)
                }
            }
        }
    }
}

@Suppress("FunctionNaming")
@Composable
fun CraftableView(it: MessageElement.Craftable) {

}

@Suppress("FunctionNaming")
@Composable
fun ActionView(it: MessageElement.Action) {

}

@Suppress("FunctionNaming")
@Composable
fun FoodView(it : MessageElement.Food) {

}

@Suppress("FunctionNaming")
@Composable
fun NormalView(it : MessageElement.Normal) {

}

@Suppress("FunctionNaming")
@Composable
fun WeaponView(it : MessageElement.Weapon) {

}
 */