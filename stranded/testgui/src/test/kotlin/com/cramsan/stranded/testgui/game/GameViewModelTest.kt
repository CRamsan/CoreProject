package com.cramsan.stranded.testgui.game

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.logic.MutableStrandedGameState
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.server.JvmClient
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.GameStateMessage
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author cramsan
 */
@ExperimentalCoroutinesApi
class GameViewModelTest : TestBase() {

    lateinit var viewModel: GameViewModel

    @MockK(relaxed = true)
    lateinit var client: JvmClient

    override fun setupTest() {
        viewModel = GameViewModel(
            client,
            testCoroutineRule.testCoroutineDispatcher,
        )
        viewModel.onServerEventReceived(Connected(playerId))
    }

    @Test
    fun testInitialGameState() = runBlockingTest {
        viewModel.onServerEventReceived(GameStateMessage(testGameState))

        assertEquals(5, viewModel.health.value)
        assertEquals("cramsan", viewModel.name.value)
        assertEquals(Phase.FORAGING, viewModel.phase.value)
    }

    companion object {
        val playerId = "playerId1"
        val testGameState = MutableStrandedGameState(
            gamePlayers = mutableListOf(
                GamePlayer(
                    id = playerId,
                    nane = "cramsan",
                    health = 5,
                )
            ),
            scavengeStack = mutableListOf(),
            nightStack = mutableListOf(),
            belongingsStack = mutableListOf(),
            shelters = mutableListOf(),
            hasFire = false,
            isFireBlocked = false,
            night = 0,
            phase = Phase.FORAGING,
        )
    }
}
