package com.cramsan.stranded.gdx

import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GameController(
    cardRepository: CardRepository,
    dispatcher: CoroutineDispatcher,
) {

    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    val game = Game(
        gameScope = scope,
        startingNightCards = cardRepository.readNightCards().map { holder ->
            (0..holder.quantity)
                .mapNotNull { holder.content }
        }
            .flatten()
            .shuffled(),
        startingForageCards = cardRepository.readForageCards().map { holder ->
            (0..holder.quantity)
                .mapNotNull { holder.content }
        }
            .flatten()
            .shuffled(),
        startingBelongingCards = cardRepository.readBelongingCards().map { holder ->
            (0..holder.quantity)
                .mapNotNull { holder.content }
        }
            .flatten()
            .shuffled(),
    )

    fun onShow() {
    }

    fun onDispose() {
    }
}
