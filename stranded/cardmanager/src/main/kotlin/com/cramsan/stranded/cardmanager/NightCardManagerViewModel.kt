package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NightCardManagerViewModel(
    cardRepository: CardRepository,
) : BaseCardManagerViewModel<NightEvent>(cardRepository) {

    override fun onShow() = runBlocking {
        withContext(Dispatchers.IO) {
            _deck.value = cardRepository.readNightCards().toMutableCardHolderList()
        }
        initializeDeck()
    }

    override fun onSave() = runBlocking {
        refreshSelectedCard()
        withContext(Dispatchers.IO) {
            cardRepository.saveNightCards(_deck.value)
            _deck.value = cardRepository.readNightCards().toMutableCardHolderList()
        }
        onCardSelected(selectedCardIndex.value)
    }

    override fun onNew() {
        super.onNew()
    }

    override fun refreshSelectedCard() {
        cleanInput()
        selectedCard.value.content = instanciateNewCard()
        selectedCard.value.quantity = cardQuantity.value.toInt()
    }

    override fun instanciateNewCard(): NightEvent {
        return NightEvent(
            cardTitle.value,
            emptyList(),
        )
    }
}
