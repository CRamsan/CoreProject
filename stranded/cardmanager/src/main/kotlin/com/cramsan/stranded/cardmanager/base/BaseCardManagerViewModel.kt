package com.cramsan.stranded.cardmanager.base

import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class BaseCardManagerViewModel<T : Card>(
    protected val cardRepository: CardRepository,
    protected val scope: CoroutineScope,
) : CardEventHandler {

    abstract val tabTitle: String

    protected val _deck = MutableStateFlow<List<CardHolder<T>>>(listOf())
    val deck: StateFlow<List<CardHolder<T>>> = _deck

    private val _selectedCardIndex = MutableStateFlow(0)
    val selectedCardIndex: StateFlow<Int> = _selectedCardIndex

    private val _cardQuantity = MutableStateFlow(0)
    val cardQuantity: StateFlow<Int> = _cardQuantity

    private val _cardTitle = MutableStateFlow("")
    val cardTitle: StateFlow<String> = _cardTitle

    fun onShow() = runBlocking {
        withContext(Dispatchers.IO) {
            _deck.value = readDeckFromRepository()
        }
        initializeDeck()
    }

    abstract fun readDeckFromRepository(): List<CardHolder<T>>

    protected val onSelectedCardIndexChange = {
        val newSelectedCard = _deck.value.elementAtOrNull(_selectedCardIndex.value)
        _cardTitle.value = newSelectedCard?.content?.title ?: "New title"
        _cardQuantity.value = newSelectedCard?.quantity ?: 0
        if (newSelectedCard == null) {
            _deck.value = _deck.value + CardHolder(null, 0)
        }
        loadCardAtIndex(_selectedCardIndex.value)
    }

    init {
        _selectedCardIndex.onEach {
            onSelectedCardIndexChange()
        }.launchIn(scope)
    }

    override fun onSaveDeckSelected() = runBlocking {
        saveCardToDeck()
        withContext(Dispatchers.IO) {
            writeDeckToRepository(_deck.value)
            _deck.value = readDeckFromRepository()
        }
        onCardAtIndexSelected(selectedCardIndex.value)
    }

    abstract fun writeDeckToRepository(deck: List<CardHolder<T>>)

    override fun onNewCardSelected() {
        saveCardToDeck()
        _selectedCardIndex.value = _deck.value.size
    }

    override fun onRemoveCardSelected() {
        val newDeck = _deck.value.toMutableList()
        newDeck.removeAt(selectedCardIndex.value)
        _deck.value = newDeck
        if (_deck.value.isNotEmpty() && selectedCardIndex.value > _deck.value.lastIndex) {
            _selectedCardIndex.value = selectedCardIndex.value - 1
        } else {
            onSelectedCardIndexChange()
        }
    }

    protected fun saveCardToDeck() {
        val newDeck = _deck.value.toMutableList()
        newDeck[_selectedCardIndex.value] =
            CardHolder(
                instanciateNewCard(),
                _cardQuantity.value,
            )
        _deck.value = newDeck
    }

    protected abstract fun instanciateNewCard(): T

    private fun sanitizeBaseInput() {
        _cardTitle.value = _cardTitle.value.trim()
        sanitizeInput()
    }

    abstract fun sanitizeInput()

    protected fun initializeDeck() {
        onSelectedCardIndexChange()
    }

    override fun onCardAtIndexSelected(index: Int) {
        saveCardToDeck()
        _selectedCardIndex.value = index
    }

    protected abstract fun loadCardAtIndex(index: Int)

    override fun onTitleFieldUpdated(title: String) {
        _cardTitle.value = title
    }

    override fun onQuantityTitleUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            if (quantity.isNullOrBlank()) {
                _cardQuantity.value = 0
            }
            return
        }

        _cardQuantity.value = newQuantity
    }
}
