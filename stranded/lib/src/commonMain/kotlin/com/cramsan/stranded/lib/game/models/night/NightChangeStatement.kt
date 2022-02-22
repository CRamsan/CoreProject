package com.cramsan.stranded.lib.game.models.night

import kotlinx.serialization.Serializable

/**
 * Base class that represents change configuration provided by a [NightEvent].
 */
@Serializable
sealed class NightChangeStatement(val priority: Int)

@Serializable
object CancellableByFire : NightChangeStatement(0)

@Serializable
object DestroyShelter : NightChangeStatement(10)

@Serializable
object FireUnavailableTomorrow : NightChangeStatement(30)

@Serializable
data class SelectTargetOnlyUnsheltered(val onlyUnsheltered: Boolean) : NightChangeStatement(40)

@Serializable
data class SelectTargetQuantity(val affectedPlayers: Int) : NightChangeStatement(45)

@Serializable
object SelectTargetQuantityAll : NightChangeStatement(47)

@Serializable
data class CancellableByWeapon(val change: Int, val damage: Int) : NightChangeStatement(50)

@Serializable
data class ForageCardLost(val cardsLost: Int) : NightChangeStatement(60)

@Serializable
object FiberLost : NightChangeStatement(70)

@Serializable
data class DamageToDo(val healthChange: Int) : NightChangeStatement(90)

@Serializable
object Survived : NightChangeStatement(100)
