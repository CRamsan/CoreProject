package com.cramsan.stranded.cardmanager

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import com.cramsan.stranded.lib.storage.DeckHolder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileBasedCardRepository(
    private val filename: String,
    private val json: Json,
) : CardRepository {

    private lateinit var file: File

    private lateinit var deck: DeckHolder

    private var initialized = false

    fun initialize() {
        val newFile = File(filename)
        var performSave = false

        val newDeck: DeckHolder = if (!newFile.exists()) {
            newFile.createNewFile()
            performSave = true
            DeckHolder(
                emptyList(),
                emptyList(),
                emptyList(),
            )
        } else {
            val content = newFile.readText()
            json.decodeFromString(content)
        }

        file = newFile
        deck = newDeck

        if (performSave) {
            saveFile(newDeck)
        }

        initialized = true
    }

    private fun saveFile(deckHolder: DeckHolder) {
        val newContent = json.encodeToString(deckHolder)
        file.writeText(newContent)
    }

    override fun readForageCards(): List<CardHolder<ScavengeResult>> {
        if (!initialized) TODO()

        return deck.forageCards
    }

    override fun saveForageCards(list: List<CardHolder<ScavengeResult>>) {
        if (!initialized) TODO()

        deck = deck.copy(forageCards = list)
        saveFile(deck)
    }

    override fun readNightCards(): List<CardHolder<NightEvent>> {
        if (!initialized) TODO()

        return deck.nightCards
    }

    override fun saveNightCards(list: List<CardHolder<NightEvent>>) {
        if (!initialized) TODO()

        deck = deck.copy(nightCards = list)
        saveFile(deck)
    }

    override fun readBelongingCards(): List<CardHolder<Belongings>> {
        if (!initialized) TODO()

        return deck.belongingsCards
    }

    override fun saveBelongingCards(list: List<CardHolder<Belongings>>) {
        if (!initialized) TODO()

        deck = deck.copy(belongingsCards = list)
        saveFile(deck)
    }
}
