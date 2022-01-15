package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.cardmanager.base.CardEventHandler
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import com.cramsan.stranded.lib.storage.MutableCardHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseCardManagerViewModel<T : Card>(
    protected val cardRepository: CardRepository,
) : CardEventHandler {

    protected val _deck = MutableStateFlow<MutableList<MutableCardHolder<T>>>(mutableListOf())
    val deck: StateFlow<List<CardHolder<T>>> = _deck

    protected val selectedCard = MutableStateFlow<MutableCardHolder<T>>(MutableCardHolder(null, 0))

    private val _selectedCardIndex = MutableStateFlow(0)
    val selectedCardIndex: StateFlow<Int> = _selectedCardIndex

    private val _cardQuantity = MutableStateFlow("")
    val cardQuantity: StateFlow<String> = _cardQuantity

    protected val _cardTitle = MutableStateFlow("")
    val cardTitle: StateFlow<String> = _cardTitle

    abstract fun onShow()

    override fun onNew() {
        _cardQuantity.value = "0"
        _cardTitle.value = "New title"

        selectedCard.value = MutableCardHolder(instanciateNewCard(), 0)

        _deck.value.add(selectedCard.value)

        _selectedCardIndex.value = _deck.value.lastIndex
        onCardSelected(selectedCardIndex.value)
    }

    override fun onRemove() {
        _deck.value.removeAt(selectedCardIndex.value)
        _selectedCardIndex.value = minOf(_deck.value.lastIndex, selectedCardIndex.value)
        onCardSelected(selectedCardIndex.value)
    }

    abstract fun instanciateNewCard(): T

    open fun cleanInput() {
        _cardTitle.value = _cardTitle.value.trim()
        _cardQuantity.value = try {
            _cardQuantity.value.toInt()
        } catch (throwable: Throwable) {
            0
        }.toString()
    }

    protected fun initializeDeck() {
        if (_deck.value.isEmpty()) {
            onNew()
        }
    }

    override fun onCardSelected(index: Int) {
        refreshSelectedCard()

        _selectedCardIndex.value = index

        val newSelectedCard = _deck.value[index]

        selectedCard.value = newSelectedCard
        onTitleUpdated(newSelectedCard.content?.title ?: "New title")
        onQuantityUpdated(newSelectedCard.quantity.toString())
    }

    abstract fun refreshSelectedCard()

    override fun onTitleUpdated(title: String) {
        _cardTitle.value = title
    }

    override fun onQuantityUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _cardQuantity.value = newQuantity.toString()
    }
}
