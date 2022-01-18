package com.cramsan.stranded.lib.game.models.common

import com.cramsan.stranded.lib.utils.generateUUID
import kotlinx.serialization.Serializable

/**
 * This class works as a foundation for all classes that work as a card and that are expected to be back of a stack
 * of cards.
 * The default value for the [id] is UUID string.
 */
@Serializable
abstract class Card {
    val id: String = generateUUID()
    abstract val title: String
}
