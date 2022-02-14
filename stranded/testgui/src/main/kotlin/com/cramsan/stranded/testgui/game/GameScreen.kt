package com.cramsan.stranded.testgui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Name: $name")
        Text("Health: $health")
        Text("Phase: $phase")
        Row {
            TextField(
                modifier = Modifier.weight(1f),
                value = quantity.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onCardQuantityUpdated(it) }
            )
            Button({ viewModel.onForageSelected() }) {
                Text("Forage")
            }
        }
        Button({ viewModel.onListPlayersSelected() }) {
            Text("Refresh players")
        }
        Row {
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
        Button({ viewModel.onEndTurnSelected() }) {
            Text("End Turn")
        }
        Row {
            Column(modifier = Modifier.weight(1f)) {
                belongings?.forEach {
                    Button({ viewModel.onCardSelected(it.id) }) {
                        Text(it.title)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                scavengeResults?.forEach {
                    Button({ viewModel.onCardSelected(it.id) }) {
                        Text(it.title)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                craftables?.forEach {
                    Button({ viewModel.onCardSelected(it.id) }) {
                        Text(it.title)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                shelters?.forEach {
                    Button({ viewModel.onCardSelected(it.id) }) {
                        Text(it.id)
                    }
                }
            }
        }
    }
}
