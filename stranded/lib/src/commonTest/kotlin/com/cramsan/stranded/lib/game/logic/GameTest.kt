package com.cramsan.stranded.lib.game.logic

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.SelectCard
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.crafting.Spear
import com.cramsan.stranded.lib.game.models.night.CancellableByFire
import com.cramsan.stranded.lib.game.models.night.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.night.Survived
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.server.repository.Player
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author cramsan
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameTest : TestBase() {

    lateinit var game: Game

    override fun setupTest() {
        game = Game(
            testCoroutineScope,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    }

    @Test
    fun testSurviving() = runBlockingTest {
        game.onConfigureGame(listOf(createPlayer("1"), createPlayer("2")))
        game.setGameState(createGameState())

        launch {
            game.processNightEvent(NightEvent("", listOf(Survived)))
        }

        completeTurn(listOf("1", "2"))

        assertTrue(game.gameCompleted)
    }

    @Test
    fun testIgnoreByFire() = runBlockingTest {
        game.onConfigureGame(listOf(createPlayer("1"), createPlayer("2")))
        game.setGameState(
            createGameState(
                hasFire = true,
            )
        )

        launch {
            game.processNightEvent(NightEvent("", listOf(CancellableByFire, Survived)))
        }

        completeTurn(listOf("1", "2"))

        assertFalse(game.gameCompleted)
    }

    @Test
    fun testCancellableByWeapon() = runBlockingTest {
        val spear = Spear()
        game.onConfigureGame(listOf(createPlayer("1"), createPlayer("2")))
        game.setGameState(
            createGameState(
                gamePlayers = mutableListOf(
                    createGamePlayer(
                        "1",
                        craftables = mutableListOf(spear)
                    ),
                    createGamePlayer(
                        "2",
                        craftables = mutableListOf(spear)
                    )
                ),
            )
        )

        launch {
            game.processNightEvent(
                NightEvent(
                    "",
                    listOf(
                        SelectTargetQuantityAll,
                        CancellableByWeapon(+1, -1),
                    )
                )
            )
        }

        game.onPlayerIntentReceived("1", SelectCard("1", spear.id))
        game.onPlayerIntentReceived("2", SelectCard("2", ""))

        completeTurn(listOf("1", "2"))

        assertEquals(true, getPlayer("1")?.craftables?.isEmpty())
        assertEquals(false, getPlayer("2")?.craftables?.isEmpty())
        assertEquals(4, getPlayer("1")?.health)
        assertEquals(3, getPlayer("2")?.health)
    }

    private suspend fun completeTurn(users: List<String> = listOf("1", "2")) {
        delay(10)
        users.forEach {
            game.onPlayerIntentReceived(it, EndTurn)
        }
    }

    private fun getPlayer(id: String) = game.gameState.gamePlayers.find { id == it.id }

    companion object {

        fun createPlayer(id: String, nane: String = "Player-$id") = Player(id, nane, true)

        fun createGamePlayer(
            id: String,
            nane: String = "Player-$id",
            health: Int = 4,
            belongings: MutableList<Belongings> = mutableListOf(),
            scavengeResults: MutableList<ScavengeResult> = mutableListOf(),
            craftables: MutableList<Craftable> = mutableListOf(),
        ): GamePlayer = GamePlayer(
            id,
            nane,
            health,
            belongings,
            scavengeResults,
            craftables,
        )

        fun createGameState(
            gamePlayers: MutableList<GamePlayer> = mutableListOf(createGamePlayer("1"), createGamePlayer("2")),
            scavengeStack: MutableList<ScavengeResult> = mutableListOf(),
            nightStack: MutableList<NightEvent> = mutableListOf(),
            belongingsStack: MutableList<Belongings> = mutableListOf(),
            shelters: MutableList<Shelter> = mutableListOf(),
            hasFire: Boolean = false,
            isFireBlocked: Boolean = false,
            night: Int = 1,
            phase: Phase = Phase.NIGHT
        ): StrandedGameState = MutableStrandedGameState.toSnapshot(
            MutableStrandedGameState(
                gamePlayers,
                scavengeStack,
                nightStack,
                belongingsStack,
                shelters,
                hasFire,
                isFireBlocked,
                night,
                phase,
            )
        )
    }
}
