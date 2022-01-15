package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.game.models.scavenge.Consumable
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.scavenge.Useless
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ForageCardManagerViewModel(
    cardRepository: CardRepository,
) : BaseCardManagerViewModel<ScavengeResult>(cardRepository) {

    override fun onShow() = runBlocking {
        withContext(Dispatchers.IO) {
            _deck.value = cardRepository.readForageCards().toMutableCardHolderList()
        }
        initializeDeck()
    }

    override fun onSave() = runBlocking {
        cleanInput()
        refreshSelectedCard()
        withContext(Dispatchers.IO) {
            cardRepository.saveForageCards(_deck.value)
            _deck.value = cardRepository.readForageCards().toMutableCardHolderList()
        }
        onCardSelected(selectedCardIndex.value)
    }

    override fun onNew() {
        super.onNew()

        refreshSelectedCard()
    }

    fun setConsumableType() {
        selectedCard.value.content = Consumable("", 0, 0, Status.NORMAL, 0)
    }

    fun setResourceType() {
        selectedCard.value.content = Resource(ResourceType.BONE)
    }

    fun setUselessType() {
        selectedCard.value.content = Useless("")
    }

    override fun refreshSelectedCard() {
        cleanInput()
        selectedCard.value.content = instanciateNewCard()
        selectedCard.value.quantity = cardQuantity.value.toInt()
    }

    override fun instanciateNewCard(): ScavengeResult {
        return selectedCard.value.content?.let {
            when (it) {
                is Consumable -> Consumable(cardTitle.value, 0, 0, Status.NORMAL, 0)
                is Resource -> Resource(ResourceType.BONE)
                is Useless -> Useless(cardTitle.value)
            }
        } ?: TODO()
    }
}
