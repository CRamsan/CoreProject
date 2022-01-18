package com.cramsan.stranded.lib.storage

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult

interface CardRepository {

    fun readForageCards(): List<CardHolder<ScavengeResult>>

    fun saveForageCards(list: List<CardHolder<ScavengeResult>>)

    fun readNightCards(): List<CardHolder<NightEvent>>

    fun saveNightCards(list: List<CardHolder<NightEvent>>)

    fun readBelongingCards(): List<CardHolder<Belongings>>

    fun saveBelongingCards(list: List<CardHolder<Belongings>>)
}
