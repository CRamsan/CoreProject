package com.cramsan.stranded.cardmanager.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.storage.CardHolder

@Composable
fun CardManager(
    tabTitles: List<String>,
    tabContent: @Composable (Int) -> Unit,
) {
    MaterialTheme {
        var tabIndex by remember { mutableStateOf(0) }
        Column {
            TabRow(selectedTabIndex = tabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }
            tabContent(tabIndex)
        }
    }
}

@Composable
fun <T : Card> CardList(
    selectedIndex: Int,
    cardDeck: List<CardHolder<T>>,
    modifier: Modifier = Modifier,
    onCardSelected: (index: Int) -> Unit,
) {

    Column {
        Text(
            text = "Card list",
            modifier = Modifier.padding(10.dp),
        )
        LazyColumn(
            modifier = modifier
                .width(200.dp)
                .weight(1f)
        ) {
            itemsIndexed(cardDeck) { index, item ->
                val selected = index == selectedIndex
                val title = item.content?.title ?: "New title"
                Text(
                    text = "$title x ${item.quantity}",
                    fontWeight = if (selected) { FontWeight.Bold } else { FontWeight.Normal },
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    maxLines = 1,
                    modifier = Modifier
                        .selectable(
                            selected = selected,
                            onClick = { onCardSelected(index) }
                        )
                        .fillMaxWidth()
                        .padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun <T : Card> Content(
    selectedIndex: Int,
    cardDeck: List<CardHolder<T>>,
    handler: CardEventHandler,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            CardList(
                selectedIndex = selectedIndex,
                cardDeck = cardDeck,
                onCardSelected = { index -> handler.selectedCardAtIndex(index) },
            )
            Box {
                content()
            }
        }
        BottomBar(
            onNew = { handler.newCard() },
            onSave = { handler.saveDeck() },
            onDelete = { handler.removeCard() },
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onNew: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        Button(
            onClick = onNew,
            modifier = Modifier.padding(5.dp)
        ) {
            Text("New")
        }
        Button(
            onClick = onDelete,
            modifier = Modifier.padding(5.dp)
        ) {
            Text("Delete")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onSave,
            modifier = Modifier.padding(5.dp)
        ) {
            Text("Save")
        }
    }
}

interface CardEventHandler {
    fun selectedCardAtIndex(index: Int)
    fun newCard()
    fun saveDeck()
    fun removeCard()
    fun updateTitle(title: String)
    fun updateQuantity(quantity: String)
}
