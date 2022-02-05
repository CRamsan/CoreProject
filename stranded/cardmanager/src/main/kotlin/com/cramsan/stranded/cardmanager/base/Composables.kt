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

/**
 * Main frame for the CardManager. This is a tabbed layout controlled by the [tabTitles]. Based on
 * the selected tab the [tabContent] will display the intended content.
 */
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

/**
 * Display the frame for a selected tab. This will include a bottom bar and a card list.
 * The [content] will be displayed as the main content of this screen.
 */
@Composable
fun <T : Card> TabFrame(
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
                onCardSelected = { index -> handler.onCardAtIndexSelected(index) },
            )
            Box {
                content()
            }
        }
        BottomBar(
            onNew = { handler.onNewCardSelected() },
            onSave = { handler.onSaveDeckSelected() },
            onRemove = { handler.onRemoveCardSelected() },
        )
    }
}

/**
 * Render a list of cards of type [T].
 */
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
                CardHolderItem(
                    item,
                    index,
                    selected,
                    onCardSelected,
                )
            }
        }
    }
}

/**
 * Render a card as part of a list.
 */
@Composable
fun <T : Card> CardHolderItem(
    cardHolder: CardHolder<T>,
    cardIndex: Int,
    selected: Boolean,
    onCardSelected: (index: Int) -> Unit,
) {
    val title = cardHolder.content?.title ?: "New title"
    Text(
        text = "$title x ${cardHolder.quantity}",
        fontWeight = if (selected) { FontWeight.Bold } else { FontWeight.Normal },
        overflow = TextOverflow.Ellipsis,
        softWrap = false,
        maxLines = 1,
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = { onCardSelected(cardIndex) }
            )
            .fillMaxWidth()
            .padding(5.dp)
    )
}

/**
 * Render a bottom bar with some global actions such as Save, Remove, New.
 */
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onNew: () -> Unit,
    onRemove: () -> Unit,
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
            onClick = onRemove,
            modifier = Modifier.padding(5.dp)
        ) {
            Text("Remove")
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

/**
 * Handler for all interactions that come from the UI layer.
 */
interface CardEventHandler {
    /**
     * The card at position [index] was selected by the user.
     */
    fun onCardAtIndexSelected(index: Int)

    /**
     * The user pressed the New button.
     */
    fun onNewCardSelected()

    /**
     * The user pressed the Save button.
     */
    fun onSaveDeckSelected()

    /**
     * The user pressed the Remove button.
     */
    fun onRemoveCardSelected()

    /**
     * The field containing the title was updated with a new string [title].
     */
    fun onTitleFieldUpdated(title: String)

    /**
     * The field containing the card quantity was updated with a new string [quantity].
     */
    fun onQuantityTitleUpdated(quantity: String)
}
