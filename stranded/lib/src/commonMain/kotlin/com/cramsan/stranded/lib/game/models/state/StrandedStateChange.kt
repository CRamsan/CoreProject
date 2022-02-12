package com.cramsan.stranded.lib.game.models.state

import com.cramsan.stranded.lib.game.logic.StrandedGameState
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.server.game.StateChange
import kotlinx.serialization.Serializable

// TODO: Document this file and ensure names for classes and arguments are appropriate.

/**
 * Base class that represents low level changes to the [StrandedGameState].
 */
@Serializable
sealed class StrandedStateChange : StateChange()

@Serializable
class SingleHealthChange(val playerId: String, val healthChange: Int) : StrandedStateChange()

@Serializable
data class DrawBelongingCard(val playerId: String) : StrandedStateChange()

@Serializable
data class DrawScavengeCard(val playerId: String) : StrandedStateChange()

@Serializable
object DrawNightCard : StrandedStateChange()

@Serializable
object IncrementNight : StrandedStateChange()

@Serializable
data class SetPhase(val gamePhase: Phase) : StrandedStateChange()

@Serializable
data class UserCard(val playerId: String, val cardId: String) : StrandedStateChange()

@Serializable
data class CraftCard(val playerId: String, val targetList: List<String>, val craftable: Craftable) : StrandedStateChange()

@Serializable
object ExtinguishFire : StrandedStateChange()

@Serializable
data class SetFireBlockStatus(val blockFire: Boolean) : StrandedStateChange()

@Serializable
object DestroyShelter : StrandedStateChange()

@Serializable
data class LoseCard(val playerId: String, val cardId: String) : StrandedStateChange()