package com.cramsan.stranded.cardmanager.foragecards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cramsan.stranded.cardmanager.base.TabFrame
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType

/**
 * Content for the Forage Cards tab. The state will be managed by the [viewModel].
 */
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

    TabFrame(selectedIndex.value, cardDeck.value, viewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title.value,
                enabled = cardType.value != ForageCardManagerViewModel.CARD_TYPES[1],
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.onTitleFieldUpdated(it) },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = quantity.value.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onQuantityTitleUpdated(it) },
            )

            val radioGroupOptions = ForageCardManagerViewModel.CARD_TYPES
            Row {
                radioGroupOptions.forEach { text ->
                    val selected = text == cardType.value
                    Row(
                        Modifier
                            .clickable { viewModel.setCardType(text) }
                            .padding(horizontal = 16.dp),
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp),
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
                            .padding(horizontal = 16.dp),
                    ) {
                        RadioButton(
                            selected = selected,
                            onClick = null,
                            enabled = enabled,
                        )
                        Text(
                            text = text.name,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = remainingUses.value?.toString() ?: "-",
                label = { Text(text = "Remaining uses") },
                enabled = remainingUses.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingUsesUpdated(it) },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = healthModifier.value?.toString() ?: "-",
                label = { Text(text = "Health modifier") },
                enabled = healthModifier.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onHealthModifierUpdated(it) },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = remainingDays.value?.toString() ?: "-",
                label = { Text(text = "Remaining days") },
                enabled = remainingDays.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onRemainingDaysUpdated(it) },
            )
        }
    }
}
