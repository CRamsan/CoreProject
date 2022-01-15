package com.cramsan.stranded.cardmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.stranded.cardmanager.base.CardManager
import com.cramsan.stranded.cardmanager.base.Content
import com.cramsan.stranded.lib.messages.module
import kotlinx.serialization.json.Json

@Composable
fun NightCardsTab(
    viewModel: NightCardManagerViewModel
) {
    val title = viewModel.cardTitle.collectAsState()
    val selectedIndex = viewModel.selectedCardIndex.collectAsState()
    val quantity = viewModel.cardQuantity.collectAsState()
    val cardDeck = viewModel.deck.collectAsState()
    Content(selectedIndex.value, cardDeck.value, viewModel) {
        Column {
            TextField(
                value = title.value,
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.onTitleUpdated(it) }
            )
            TextField(
                value = quantity.value,
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onQuantityUpdated(it) }
            )
        }
    }
}

@Composable
fun ForageCardsTab(
    viewModel: ForageCardManagerViewModel,
) {
    val title = viewModel.cardTitle.collectAsState()
    val selectedIndex = viewModel.selectedCardIndex.collectAsState()
    val quantity = viewModel.cardQuantity.collectAsState()
    val cardDeck = viewModel.deck.collectAsState()
    Content(selectedIndex.value, cardDeck.value, viewModel) {
        Column {
            TextField(
                value = title.value,
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.onTitleUpdated(it) }
            )
            TextField(
                value = quantity.value,
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onQuantityUpdated(it) }
            )
        }
    }
}

@Composable
fun BelongingsCardsTab(
    viewModel: BelongingCardManagerViewModel
) {
    val title = viewModel.cardTitle.collectAsState()
    val selectedIndex = viewModel.selectedCardIndex.collectAsState()
    val quantity = viewModel.cardQuantity.collectAsState()
    val cardDeck = viewModel.deck.collectAsState()
    val cardType = viewModel.cardType.collectAsState()
    val remainingUses = viewModel.remainingUses.collectAsState()
    val healthModifier = viewModel.healthModifier.collectAsState()
    val remainingDays = viewModel.remainingDays.collectAsState()

    Content(selectedIndex.value, cardDeck.value, viewModel) {
        Column {
            TextField(
                value = title.value,
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.onTitleUpdated(it) }
            )
            TextField(
                value = quantity.value,
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onQuantityUpdated(it) }
            )

            val radioGroupOptions = BelongingCardManagerViewModel.CARD_TYPES
            Row {
                radioGroupOptions.forEach { text ->
                    Row(
                        Modifier.selectable(
                            selected = (text == cardType.value),
                            onClick = { viewModel.setCardType(text) }
                        )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == cardType.value),
                            onClick = { viewModel.setCardType(text) }
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            TextField(
                value = remainingUses.toString(),
                label = { Text(text = "Remaining uses") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingUsesUpdated(it) }
            )
            TextField(
                value = healthModifier.toString(),
                label = { Text(text = "Health modifier") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onHealthModifierUpdated(it) }
            )
            TextField(
                value = remainingDays.toString(),
                label = { Text(text = "Remaining days") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingDaysUpdated(it) }
            )
        }
    }
}

fun main() = application {
    val cardRepository = FileBasedCardRepository(
        filename = "test.json",
        json = Json {
            serializersModule = module
        }
    )

    val belongingViewModel = BelongingCardManagerViewModel(cardRepository)
    val forageViewModel = ForageCardManagerViewModel(cardRepository)
    val nightViewModel = NightCardManagerViewModel(cardRepository)

    Window(onCloseRequest = ::exitApplication) {
        CardManager(listOf("Night Cards", "Forage Cards", "Belonging Cards")) { index ->
            when (index) {
                0 -> NightCardsTab(nightViewModel)
                1 -> ForageCardsTab(forageViewModel)
                2 -> BelongingsCardsTab(belongingViewModel)
            }
        }
    }

    cardRepository.initialize()
    // forageViewModel.onShow()
    belongingViewModel.onShow()
    nightViewModel.onShow()
}
