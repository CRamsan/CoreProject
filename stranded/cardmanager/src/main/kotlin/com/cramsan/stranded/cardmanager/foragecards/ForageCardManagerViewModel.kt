package com.cramsan.stranded.cardmanager.foragecards

import com.cramsan.stranded.cardmanager.base.BaseCardManagerViewModel
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.game.models.scavenge.Consumable
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.scavenge.Useless
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForageCardManagerViewModel(
    cardRepository: CardRepository,
    scope: CoroutineScope,
) : BaseCardManagerViewModel<ScavengeResult>(cardRepository, scope) {

    override val tabTitle = "Forage Cards"

    private val _cartType = MutableStateFlow(CARD_TYPES.first())
    val cardType: StateFlow<String> = _cartType

    private val _resourceType = MutableStateFlow<ResourceType?>(null)
    val resourceType: StateFlow<ResourceType?> = _resourceType

    private val _remainingDays = MutableStateFlow<Int?>(null)
    val remainingDays: StateFlow<Int?> = _remainingDays

    private val _healthModifier = MutableStateFlow<Int?>(null)
    val healthModifier: StateFlow<Int?> = _healthModifier

    private val _remainingUses = MutableStateFlow<Int?>(null)
    val remainingUses: StateFlow<Int?> = _remainingUses

    override fun readDeckFromRepository(): List<CardHolder<ScavengeResult>> {
        return cardRepository.readForageCards()
    }

    override fun writeDeckToRepository(deck: List<CardHolder<ScavengeResult>>) {
        cardRepository.saveForageCards(deck)
    }

    fun setCardType(cardType: String) {
        _cartType.value = cardType
        saveCardToDeck()
        onSelectedCardIndexChange()
    }

    fun setResourceType(resourceType: ResourceType) {
        if (_cartType.value != CARD_TYPES[1]) {
            return
        }

        _resourceType.value = resourceType
        onTitleFieldUpdated(resourceType.name)
    }

    fun onRemainingUsesUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _remainingUses.value = newQuantity
    }

    fun onRemainingDaysUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _remainingDays.value = newQuantity
    }

    fun onHealthModifierUpdated(quantity: String) {
        val newQuantity: Int = try {
            quantity.toInt()
        } catch (throwable: Throwable) {
            return
        }

        _healthModifier.value = newQuantity
    }

    override fun sanitizeInput() = Unit

    override fun instanciateNewCard(): ScavengeResult {
        return when (cardType.value) {
            CARD_TYPES[0] -> Consumable(
                cardTitle.value,
                _remainingDays.value ?: 0,
                _healthModifier.value ?: 0,
                Status.NORMAL,
                _remainingUses.value ?: 0
            )
            CARD_TYPES[1] -> Resource(
                _resourceType.value ?: ResourceType.BONE
            )
            CARD_TYPES[2] -> Useless(cardTitle.value)
            else -> TODO()
        }
    }

    override fun loadCardAtIndex(index: Int) {
        val selectedCard = deck.value[selectedCardIndex.value].content
        _cartType.value = when (selectedCard) {
            is Consumable, null -> CARD_TYPES[0]
            is Resource -> CARD_TYPES[1]
            is Useless -> CARD_TYPES[2]
        }

        when (selectedCard) {
            is Useless, null -> {
                _remainingDays.value = null
                _healthModifier.value = null
                _remainingUses.value = null
                _resourceType.value = null
            }
            is Consumable -> {
                _remainingDays.value = selectedCard.remainingDays
                _healthModifier.value = selectedCard.healthModifier
                _remainingUses.value = selectedCard.remainingUses
                _resourceType.value = null
            }
            is Resource -> {
                _remainingDays.value = null
                _healthModifier.value = null
                _remainingUses.value = null
                _resourceType.value = selectedCard.resourceType
            }
        }
    }

    companion object {
        val CARD_TYPES = listOf(
            "Consumable",
            "Resource",
            "Useless",
        )
    }
}
