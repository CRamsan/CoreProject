package com.cramsan.stranded.lib.storage

import com.cramsan.stranded.lib.game.models.common.Card
import kotlinx.serialization.Serializable

@Serializable
data class CardHolder<T : Card>(
    val content: T?,
    val quantity: Int,
)
