package com.cramsan.stranded.cardmanager.nightcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.KeyboardType
import com.cramsan.stranded.cardmanager.base.TabFrame

/**
 * Render the content inside the Night Cards tab. The state will be provided by the [viewModel].
 */
@Composable
fun NightCardsTab(
    viewModel: NightCardManagerViewModel
) {
    val title = viewModel.cardTitle.collectAsState()
    val selectedIndex = viewModel.selectedCardIndex.collectAsState()
    val quantity = viewModel.cardQuantity.collectAsState()
    val cardDeck = viewModel.deck.collectAsState()
    TabFrame(selectedIndex.value, cardDeck.value, viewModel) {
        Column {
            TextField(
                value = title.value,
                label = { Text(text = "Card title") },
                onValueChange = { viewModel.onTitleFieldUpdated(it) }
            )
            TextField(
                value = quantity.value.toString(),
                label = { Text(text = "Card quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.onQuantityTitleUpdated(it) }
            )
        }
    }
}
