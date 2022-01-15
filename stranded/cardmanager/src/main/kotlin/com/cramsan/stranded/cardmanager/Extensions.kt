package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.MutableCardHolder

fun <T : Card> List<CardHolder<T>>.toMutableCardHolderList(): MutableList<MutableCardHolder<T>> {
    return map { MutableCardHolder(it.content, it.quantity) }.toMutableList()
}
