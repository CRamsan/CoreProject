package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope

class NightCardManagerViewModel(
    cardRepository: CardRepository,
    scope: CoroutineScope,
) : BaseCardManagerViewModel<NightEvent>(cardRepository, scope) {

    override fun readDeckFromRepository(): List<CardHolder<NightEvent>> {
        return cardRepository.readNightCards()
    }

    override fun writeDeckToRepository(deck: List<CardHolder<NightEvent>>) {
        cardRepository.saveNightCards(deck)
    }

    override fun instanciateNewCard(): NightEvent {
        return NightEvent(
            cardTitle.value,
            emptyList(),
        )
    }

    override fun loadCardAtIndex(index: Int) = Unit

    override fun sanitizeInput() = Unit
}
