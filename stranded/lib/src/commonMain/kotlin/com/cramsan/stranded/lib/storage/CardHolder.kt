package com.cramsan.stranded.lib.storage

import com.cramsan.stranded.lib.game.models.common.Card
import kotlinx.serialization.Serializable

sealed interface CardHolder<T : Card> {
    val content: T?
    val quantity: Int
}

@Serializable
data class MutableCardHolder<T : Card>(
    override var content: T?,
    override var quantity: Int,
) : CardHolder<T>
