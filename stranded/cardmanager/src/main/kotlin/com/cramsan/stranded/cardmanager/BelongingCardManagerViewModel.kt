package com.cramsan.stranded.cardmanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Equippable
import com.cramsan.stranded.lib.game.models.common.StartingFood
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class BelongingCardManagerViewModel(
    cardRepository: CardRepository,
) : BaseCardManagerViewModel<Belongings>(cardRepository) {

    private val _cartType = MutableStateFlow(CARD_TYPES.first())
    val cardType: StateFlow<String> = _cartType

    private val _remainingDays = MutableStateFlow("0")
    val remainingDays: StateFlow<String> = _remainingDays

    private val _healthModifier = MutableStateFlow("0")
    val healthModifier: StateFlow<String> = _healthModifier

    private val _remainingUses = MutableStateFlow("0")
    val remainingUses: StateFlow<String> = _remainingUses

    override fun onShow() = runBlocking {
        withContext(Dispatchers.IO) {
            _deck.value = cardRepository.readBelongingCards().toMutableCardHolderList()
        }
        initializeDeck()
    }

    override fun onSave() = runBlocking {
        refreshSelectedCard()
        withContext(Dispatchers.IO) {
            cardRepository.saveBelongingCards(_deck.value)
            _deck.value = cardRepository.readBelongingCards().toMutableCardHolderList()
        }
        onCardSelected(selectedCardIndex.value)
    }

    override fun onNew() {
        _cartType.value = CARD_TYPES.first()
        _remainingUses.value = "0"
        _remainingDays.value = "0"
        _healthModifier.value = "0"

        super.onNew()

        refreshSelectedCard()
    }

    fun setCardType(cardType: String) {
        when (cardType) {
            CARD_TYPES[0] -> {
                selectedCard.value.content = Equippable("", 0)
            }
            CARD_TYPES[1] -> {
                selectedCard.value.content = StartingFood("", 0, 0, Status.NORMAL, 0)
            }
        }
        _cartType.value = cardType
    }

    override fun onCardSelected(index: Int) {
        super.onCardSelected(index)
        _cartType.value = when (selectedCard.value.content) {
            is Equippable -> CARD_TYPES[0]
            is StartingFood -> CARD_TYPES[1]
            null -> TODO()
        }

        val selectedCard = selectedCard.value.content
        when (selectedCard) {
            is Equippable -> Unit
            is StartingFood -> {
                onRemainingDaysUpdated(selectedCard.remainingDays.toString())
                onHealthModifierUpdated(selectedCard.remainingUses.toString())
            }
            null -> TODO()
        }
        onRemainingUsesUpdated(selectedCard.remainingUses.toString())
    }

    override fun cleanInput() {
        super.cleanInput()
        if (!CARD_TYPES.contains(_cartType.value)) {
            _cartType.value = CARD_TYPES[0]
        }
        _remainingUses.value = try {
            _remainingUses.value.toInt()
        } catch (throwable: Throwable) {
            0
        }.toString()
        _remainingDays.value = try {
            _remainingDays.value.toInt()
        } catch (throwable: Throwable) {
            0
        }.toString()
        _healthModifier.value = try {
            _healthModifier.value.toInt()
        } catch (throwable: Throwable) {
            0
        }.toString()
    }

    fun onRemainingUsesUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _remainingUses.value = newQuantity.toString()
    }

    fun onRemainingDaysUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _remainingDays.value = newQuantity.toString()
    }

    fun onHealthModifierUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _healthModifier.value = newQuantity.toString()
    }

    override fun refreshSelectedCard() {
        cleanInput()
        selectedCard.value.content = instanciateNewCard()
        selectedCard.value.quantity = cardQuantity.value.toInt()
    }

    override fun instanciateNewCard(): Belongings {
        return when (cardType.value) {
            CARD_TYPES[0] -> {
                Equippable(cardTitle.value, _remainingUses.value.toInt())
            }
            CARD_TYPES[1] -> {
                StartingFood(
                    cardTitle.value,
                    _remainingDays.value.toInt(),
                    _healthModifier.value.toInt(),
                    Status.NORMAL,
                    _remainingUses.value.toInt()
                )
            }
            else -> TODO()
        }
    }

    companion object {
        val CARD_TYPES = listOf("Equippable", "Starting Food")
    }
}
