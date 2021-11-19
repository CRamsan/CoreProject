package com.cramsan.stranded.lib.game.models.state

import com.cramsan.stranded.lib.game.logic.MutableGameState
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import kotlinx.serialization.Serializable

// TODO: Document this file and ensure names for classes and arguments are appropriate.

/**
 * Base class that represents low level changes to the [MutableGameState].
 */
@Serializable
sealed class Change(val priority: Int)

@Serializable
object CancellableByFire : Change(0)

@Serializable
object DestroyShelter : Change(10)

@Serializable
class CancellableByFood(val change: Int) : Change(20)

@Serializable
object FireUnavailableTomorrow : Change(30)

@Serializable
class SelectTargetOnlyUnsheltered(val onlyUnsheltered: Boolean) : Change(40)

@Serializable
class SelectTargetQuantity(val affectedPlayers: Int) : Change(45)

@Serializable
object SelectTargetQuantityAll : Change(47)

@Serializable
class CancellableByWeapon(val change: Int) : Change(50)

@Serializable
class ForageCardLost(val affectedPlayers: Int, val cardsLost: Int) : Change(60)

@Serializable
object FiberLost : Change(70)

@Serializable
class FireModification(val change: Int) : Change(80)

@Serializable
class SingleHealthChange(val playerId: String, val healthChange: Int) : Change(90)

@Serializable
class MultiHealthChange(val playerList: List<String>, val healthChange: Int) : Change(90)

@Serializable
class AllHealthChange(val healthChange: Int) : Change(90)

@Serializable
object Survived : Change(100)

@Serializable
data class DrawBelongingCard(val playerId: String) : Change(110)

@Serializable
data class DrawScavengeCard(val playerId: String) : Change(120)

@Serializable
object DrawNightCard : Change(130)

@Serializable
object IncrementNight : Change(1000)

@Serializable
data class SetPhase(val gamePhase: Phase) : Change(1020)

@Serializable
data class UserCard(val playerId: String, val cardId: String) : Change(1020)

@Serializable
data class CraftCard(val playerId: String, val targetList: List<String>, val craftable: Craftable) : Change(1030)
