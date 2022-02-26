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
import org.jetbrains.compose.web.dom.Text

@Suppress("LongMethod", "FunctionNaming")
@Composable
fun GameScreen(
    name: String?,
    health: Int?,
    quantity: Int,
    belongings: List<Belongings>?,
    scavengeResults: List<ScavengeResult>?,
    craftables: List<Craftable>?,
    phase: Phase,
    shelters: List<Shelter>?,
    viewModel: GameViewModel,
) {
    Div {

        P { Text("Name: $name") }
        P { Text("Health: $health") }
        P { Text("Phase: $phase") }
        Input(InputType.Text, attrs = {
            value(quantity.toString())
            onInput { viewModel.onCardQuantityUpdated(it.value) }
        })
        Button(attrs = {
            onClick { viewModel.onForageSelected() }
        }) {
            Text("Forage")
        }
        Button(attrs = {
            onClick { viewModel.onListPlayersSelected() }
        }) {
            Text("Refresh players")
        }
        P {
            Button({ viewModel.onCraftSpearSelected() }) {
                Text("Craft Spear")
            }
            Button({ viewModel.onCraftBasketSelected() }) {
                Text("Craft Basket")
            }
            Button({ viewModel.onCraftShelterSelected() }) {
                Text("Craft Shelter")
            }
        }
        Button(attrs = { onClick { viewModel.onEndTurnSelected() } }) {
            Text("End Turn")
        }
        belongings?.forEach { belonging ->
            Button(attrs = { onClick { viewModel.onCardSelected(belonging.id) }}) {
                Text(belonging.title)
            }
        }
        scavengeResults?.forEach { scavengeResult ->
            Button(attrs = { onClick { viewModel.onCardSelected(scavengeResult.id) } }) {
                Text(scavengeResult.title)
            }
        }
        craftables?.forEach { craftable ->
            Button(attrs = { onClick { viewModel.onCardSelected(craftable.id) }}) {
                Text(craftable.title)
            }
        }
        shelters?.forEach { shelter ->
            Button(attrs = { onClick { viewModel.onCardSelected(shelter.id) }}) {
                Text(shelter.id)
            }
        }
    }
}
