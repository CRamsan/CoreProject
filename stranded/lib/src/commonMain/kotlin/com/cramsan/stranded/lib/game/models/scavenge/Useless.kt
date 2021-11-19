package com.cramsan.stranded.lib.game.models.scavenge

import kotlinx.serialization.Serializable

/**
 * This is a [ScavengeResult] that has not use to the player. Upon receiving, this card can be discarded.s
 */
@Serializable
class Useless : ScavengeResult()
