package com.cramsan.stranded.cardmanager.belongingcards

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

/**
 * Display the content for the Belonging Cards tab. The state will be managed by the [viewModel].
 */
@Suppress("LongMethod")
@Composable
fun BelongingsCardsTab(
    viewModel: BelongingCardManagerViewModel,
) {
    val title = viewModel.cardTitle.collectAsState()
    val selectedIndex = viewModel.selectedCardIndex.collectAsState()
    val quantity = viewModel.cardQuantity.collectAsState()
    val cardDeck = viewModel.deck.collectAsState()
    val cardType = viewModel.cardType.collectAsState()
    val remainingUses = viewModel.remainingUses.collectAsState()
    val healthModifier = viewModel.healthModifier.collectAsState()
    val remainingDays = viewModel.remainingDays.collectAsState()

    TabFrame(selectedIndex.value, cardDeck.value, viewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title.value,
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

            val radioGroupOptions = BelongingCardManagerViewModel.CARD_TYPES
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

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = remainingUses.value.toString(),
                label = { Text(text = "Remaining uses") },
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
