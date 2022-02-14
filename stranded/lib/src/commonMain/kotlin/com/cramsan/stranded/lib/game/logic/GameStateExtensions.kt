package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.MutableGamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Food
import com.cramsan.stranded.lib.game.models.common.UsableItem
import com.cramsan.stranded.lib.game.models.common.Weapon
import com.cramsan.stranded.lib.game.models.crafting.Basket
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.crafting.Fire
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.crafting.Spear
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.scavenge.Useless
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DestroyShelter
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.ExtinguishFire
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.LoseCard
import com.cramsan.stranded.lib.game.models.state.SetFireBlockStatus
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.server.MultiplayerGameEventHandler

/**
 * This is the only function to apply changes to [StrandedGameState].
 */
internal fun MutableStrandedGameState.processEvent(
    change: StrandedStateChange,
    multiplayerGameEventHandler: MultiplayerGameEventHandler? = null,
    eventHandler: GameEventHandler? = null,
) {
    when (change) {
        is SingleHealthChange -> {
            val damage = change.healthChange
            val player = getMutablePlayer(change.playerId)
            player.changeHealth(damage, eventHandler)
        }
        is DrawBelongingCard -> {
            val player = getMutablePlayer(change.playerId)
            val card = drawBelongingCard()
            player.receiveCard(card, eventHandler)
        }
        IncrementNight -> {
            gamePlayers.forEach {
                it.getFood().forEach {
                    it.itemOnNightCompleted()
                }
            }
            night++
        }
        DrawNightCard -> {
            drawNightCard()
        }
        is DrawScavengeCard -> {
            val player = getMutablePlayer(change.playerId)
            val card = drawScavengeCard()
            player.receiveCard(card, eventHandler)
        }
        is SetPhase -> phase = change.gamePhase
        is UserCard -> {
            val player = getMutablePlayer(change.playerId)

            val card = getCard(player, change.cardId)
            when (card) {
                is Food -> {
                    player.changeHealth(card.healthModifier, eventHandler)
                    card.itemUsed()
                    if (card.remainingUses <= 0) {
                        player.releaseCard(card, eventHandler)
                    }
                }
                is Weapon -> {
                    card.itemUsed()
                    if (card.remainingUses <= 0) {
                        player.releaseCard(card, eventHandler)
                    }
                }
                is Useless -> {
                    player.releaseCard(card, eventHandler)
                }
            }
        }
        is CraftCard -> {
            val player = getMutablePlayer(change.playerId)

            val cardsToLose = change.targetList.map { target ->
                getScavengeResultCard(player, target) as Resource
            }

            val requirementsMet = when (change.craftable) {
                is Basket -> TODO()
                is Fire -> TODO()
                is Shelter -> TODO()
                is Spear -> {
                    cardsToLose.getResourceCard(ResourceType.ROCK)
                    cardsToLose.getResourceCard(ResourceType.STICK)
                    true
                }
            }

            if (requirementsMet) {
                cardsToLose.forEach {
                    player.releaseCard(it, eventHandler)
                }
                player.receiveCard(change.craftable, eventHandler)
            }
        }
        ExtinguishFire -> hasFire = false
        DestroyShelter -> shelters.clear()
        is SetFireBlockStatus -> isFireBlocked = change.blockFire
        is LoseCard -> {
            val player = getMutablePlayer(change.playerId)
            val cardToLose = getCard(player, change.cardId)

            cardToLose?.let {
                player.releaseCard(it, eventHandler)
            }
        }
    }
    multiplayerGameEventHandler?.onStateChangeExecuted(change)
}

/**
 * There are all the extension functions that can make modifications to the state. They are all private.
 */
private fun MutableStrandedGameState.drawNightCard(): NightEvent = drawCard(nightStack)

private fun MutableStrandedGameState.drawScavengeCard(): ScavengeResult = drawCard(scavengeStack)

private fun MutableStrandedGameState.drawBelongingCard(): Belongings = drawCard(belongingsStack)

private fun <T : Card> drawCard(stack: MutableList<T>): T {
    return stack.removeLast()
}

private fun Shelter.clearPlayers() {
    playerList.clear()
}

private fun Shelter.removePlayer(player: GamePlayer) {
    if (playerList.isEmpty()) return

    playerList.remove(player.id)
}

private fun Shelter.addPlayer(player: GamePlayer) {
    if (playerList.size >= Shelter.MAX_OCCUPANCY) return

    playerList.add(player.id)
}

private fun Food.itemOnNightCompleted() {
    remainingDays--
}

private fun UsableItem.itemUsed() {
    remainingUses--
}

private fun <T : Card> MutableGamePlayer.releaseCard(card: T, eventHandler: GameEventHandler?): T {
    val result = when (card) {
        is Belongings -> belongings.remove(card)
        is ScavengeResult -> scavengeResults.remove(card)
        is Craftable -> craftables.remove(card)
        else -> throw IllegalArgumentException("Card type not supported")
    }
    require(result)
    eventHandler?.onCardRemoved(id, card)
    return card
}

private fun <T : Card> MutableGamePlayer.receiveCard(card: T, eventHandler: GameEventHandler?) {
    when (card) {
        is Belongings -> belongings.add(card)
        is ScavengeResult -> scavengeResults.add(card)
        is Craftable -> craftables.add(card)
    }
    eventHandler?.onCardReceived(id, card)
}

private fun MutableGamePlayer.changeHealth(damage: Int, eventHandler: GameEventHandler?) {
    health += damage
    eventHandler?.onPlayerHealthChange(id, health)
}

private fun MutableStrandedGameState.getMutablePlayer(playerId: String): MutableGamePlayer {
    return gamePlayers.find { it.id == playerId }!!
}

/**
 * These are the extensions functions to read from the [StrandedGameState]. These can be public as they do not make modifications.
 */

fun StrandedGameState.getPlayer(playerId: String): GamePlayer? {
    return gamePlayers.find { it.id == playerId }
}

fun getCard(player: GamePlayer, cardId: String): Card? {
    return player.scavengeResults.find { it.id == cardId }
        ?: player.belongings.find { it.id == cardId }
        ?: player.craftables.find { it.id == cardId }
}

fun getScavengeResultCard(player: GamePlayer, cardId: String): Card? {
    return player.scavengeResults.find { it.id == cardId }
}

fun List<ScavengeResult>.getResources(): List<Resource> {
    return filter { it is Resource }.map { it as Resource }
}

fun List<ScavengeResult>.getResourceCard(resourceType: ResourceType): Resource? {
    return getResources().find { it.resourceType == resourceType }
}

fun GamePlayer.getFood(): List<Food> {
    val allFood = mutableListOf<Food>()

    allFood += scavengeResults.filter { it is Food }.map { it as Food }
    allFood += belongings.filter { it is Food }.map { it as Food }
    allFood += craftables.filter { it is Food }.map { it as Food }

    return allFood
}
