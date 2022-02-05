package com.cramsan.stranded.cardmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.stranded.cardmanager.base.CardManager
import com.cramsan.stranded.cardmanager.base.Content
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.messages.module
import com.cramsan.stranded.lib.storage.FileBasedCardRepository
import kotlinx.coroutines.MainScope
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
                onValueChange = { viewModel.updateTitle(it) }
            )
            TextField(
                value = quantity.value.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.updateQuantity(it) }
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
    val cardType = viewModel.cardType.collectAsState()
    val resourceType = viewModel.resourceType.collectAsState()
    val remainingUses = viewModel.remainingUses.collectAsState()
    val healthModifier = viewModel.healthModifier.collectAsState()
    val remainingDays = viewModel.remainingDays.collectAsState()

    Content(selectedIndex.value, cardDeck.value, viewModel) {
        Column {
            TextField(
                value = title.value,
                enabled = cardType.value != ForageCardManagerViewModel.CARD_TYPES[1],
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.updateTitle(it) }
            )
            TextField(
                value = quantity.value.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.updateQuantity(it) }
            )

            val radioGroupOptions = ForageCardManagerViewModel.CARD_TYPES
            Row {
                radioGroupOptions.forEach { text ->
                    val selected = text == cardType.value
                    Row(
                        Modifier
                            .clickable { viewModel.setCardType(text) }
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            val resourceTypeGroupOptions = ResourceType.values().toList()
            Row {
                resourceTypeGroupOptions.forEach { text ->
                    val selected = text == resourceType.value
                    val enabled = cardType.value == radioGroupOptions[1]
                    Row(
                        Modifier
                            .clickable { viewModel.setResourceType(text) }
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = selected,
                            onClick = null,
                            enabled = enabled
                        )
                        Text(
                            text = text.name,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            TextField(
                value = remainingUses.value?.toString() ?: "-",
                label = { Text(text = "Remaining uses") },
                enabled = remainingUses.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingUsesUpdated(it) }
            )
            TextField(
                value = healthModifier.value?.toString() ?: "-",
                label = { Text(text = "Health modifier") },
                enabled = healthModifier.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onHealthModifierUpdated(it) }
            )
            TextField(
                value = remainingDays.value?.toString() ?: "-",
                label = { Text(text = "Remaining days") },
                enabled = remainingDays.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingDaysUpdated(it) }
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
                onValueChange = { viewModel.updateTitle(it) }
            )
            TextField(
                value = quantity.value.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.updateQuantity(it) }
            )

            val radioGroupOptions = BelongingCardManagerViewModel.CARD_TYPES
            Row {
                radioGroupOptions.forEach { text ->
                    val selected = text == cardType.value
                    Row(
                        Modifier
                            .clickable { viewModel.setCardType(text) }
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            TextField(
                value = remainingUses.value.toString(),
                label = { Text(text = "Remaining uses") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingUsesUpdated(it) }
            )
            TextField(
                value = healthModifier.value?.toString() ?: "-",
                label = { Text(text = "Health modifier") },
                enabled = healthModifier.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onHealthModifierUpdated(it) }
            )
            TextField(
                value = remainingDays.value?.toString() ?: "-",
                label = { Text(text = "Remaining days") },
                enabled = remainingDays.value != null,
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

    val scope = MainScope()
    val belongingViewModel = BelongingCardManagerViewModel(cardRepository, scope)
    val forageViewModel = ForageCardManagerViewModel(cardRepository, scope)
    val nightViewModel = NightCardManagerViewModel(cardRepository, scope)

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
