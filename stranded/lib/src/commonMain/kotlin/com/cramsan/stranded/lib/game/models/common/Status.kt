package com.cramsan.stranded.lib.game.models.common

import kotlinx.serialization.Serializable

/**
 * Different [Status] that can be applied to the player.
 */
@Serializable
enum class Status {
    /**
     * Player is unable to move this turn.
     */
    SLEEP,

    /**
     * Normal condition. No change.
     */
    NORMAL,
}
