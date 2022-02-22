package com.cramsan.stranded.gdx

import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.storage.CardRepository
import com.cramsan.stranded.server.MultiplayerGameFactory
import kotlinx.coroutines.CoroutineScope

/**
 * Factory class that decouples the instantiation of new instances of [MultiplayerGame] from the server.
 *
 * @author cramsan
 */
class GameFactory(
    private val scope: CoroutineScope,
    private val cardRepository: CardRepository,
) : MultiplayerGameFactory {
    override fun buildMultiplayerGame(): Game {
        return Game(
            scope,
            cardRepository.readNightCards()
                .map { holder ->
                    (0..holder.quantity)
                        .mapNotNull { holder.content }
                }
                .flatten()
                .shuffled(),
            cardRepository.readForageCards()
                .map { holder ->
                    (0..holder.quantity)
                        .mapNotNull { holder.content }
                }
                .flatten()
                .shuffled(),
            cardRepository.readBelongingCards()
                .map { holder ->
                    (0..holder.quantity)
                        .mapNotNull { holder.content }
                }
                .flatten()
                .shuffled(),
        )
    }
}
