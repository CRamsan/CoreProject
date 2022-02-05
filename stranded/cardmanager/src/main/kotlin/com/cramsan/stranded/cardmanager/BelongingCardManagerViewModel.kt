package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Equippable
import com.cramsan.stranded.lib.game.models.common.StartingFood
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BelongingCardManagerViewModel(
    cardRepository: CardRepository,
    scope: CoroutineScope,
) : BaseCardManagerViewModel<Belongings>(cardRepository, scope) {

    private val _cartType = MutableStateFlow(CARD_TYPES.first())
    val cardType: StateFlow<String> = _cartType

    private val _remainingDays = MutableStateFlow<Int?>(null)
    val remainingDays: StateFlow<Int?> = _remainingDays

    private val _healthModifier = MutableStateFlow<Int?>(null)
    val healthModifier: StateFlow<Int?> = _healthModifier

    private val _remainingUses = MutableStateFlow(0)
    val remainingUses: StateFlow<Int> = _remainingUses

    override fun readDeckFromRepository(): List<CardHolder<Belongings>> {
        return cardRepository.readBelongingCards()
    }

    override fun writeDeckToRepository(deck: List<CardHolder<Belongings>>) {
        cardRepository.saveBelongingCards(deck)
    }

    fun setCardType(cardType: String) {
        _cartType.value = cardType
        saveCardToDeck()
        onSelectedCardIndexChange()
    }

    override fun sanitizeInput() = Unit

    fun onRemainingUsesUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            if (quantity.isNullOrBlank()) {
                _remainingUses.value = 0
            }
            return
        }

        _remainingUses.value = newQuantity
    }

    fun onRemainingDaysUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            if (quantity.isNullOrBlank()) {
                _remainingDays.value = 0
            }
            return
        }

        _remainingDays.value = newQuantity
    }

    fun onHealthModifierUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            if (quantity.isNullOrBlank()) {
                _healthModifier.value = 0
            }
            return
        }

        _healthModifier.value = newQuantity
    }

    override fun instanciateNewCard(): Belongings {
        return when (cardType.value) {
            CARD_TYPES[0] -> {
                Equippable(cardTitle.value, _remainingUses.value)
            }
            CARD_TYPES[1] -> {
                StartingFood(
                    cardTitle.value,
                    _remainingDays.value ?: 0,
                    _healthModifier.value ?: 0,
                    Status.NORMAL,
                    _remainingUses.value
                )
            }
            else -> TODO()
        }
    }

    override fun loadCardAtIndex(index: Int) {
        val selectedCard = deck.value[selectedCardIndex.value].content
        _cartType.value = when (selectedCard) {
            is Equippable, null -> CARD_TYPES[0]
            is StartingFood -> CARD_TYPES[1]
        }

        when (selectedCard) {
            is Equippable, null -> {
                _remainingDays.value = null
                _healthModifier.value = null
            }
            is StartingFood -> {
                _remainingDays.value = selectedCard.remainingDays
                _healthModifier.value = selectedCard.healthModifier
            }
        }
        _remainingUses.value = selectedCard?.remainingUses ?: 0
    }

    companion object {
        val CARD_TYPES = listOf("Equippable", "Starting Food")
    }
}
