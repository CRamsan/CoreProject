package com.cramsan.awslib.entitymanager.statemachine

import com.cramsan.awslib.entitymanager.implementation.statemachine.EndTurn
import com.cramsan.awslib.entitymanager.implementation.statemachine.Move
import com.cramsan.awslib.entitymanager.implementation.statemachine.StartTurn
import com.cramsan.awslib.entitymanager.implementation.statemachine.TransitionStack
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TransitionStackTests : TestBase() {

    private lateinit var transitionStack: TransitionStack

    override fun setupTest() {
        val log: EventLoggerInterface = mockk(relaxed = true)
        EventLogger.setInstance(log)
        val transitionHandler: TransitionStack.TransitionHandler = mockk(relaxed = true)
        transitionStack = TransitionStack(transitionHandler)
    }

    /**
     * Test executing some actions
     */
    @Test
    fun `Adding actions`() = runBlockingTest {
        assertTrue(transitionStack.backStack.isEmpty())
        transitionStack.executeNewTransition(StartTurn(""))
        transitionStack.executeNewTransition(Move("", 0, 0))
        transitionStack.executeNewTransition(EndTurn(""))
        assertEquals(3, transitionStack.backStack.size)
        assertEquals(StartTurn(""), transitionStack.backStack[0])
        assertEquals(Move("", 0, 0), transitionStack.backStack[1])
        assertEquals(EndTurn(""), transitionStack.backStack[2])
    }

    /**
     * Test steping back some actions
     */
    @Test
    fun `Removing actions`() = runBlockingTest {
        transitionStack.executeNewTransition(StartTurn(""))
        transitionStack.executeNewTransition(Move("", 0, 0))
        transitionStack.executeNewTransition(EndTurn(""))
        assertEquals(3, transitionStack.backStack.size)
        assertEquals(EndTurn(""), transitionStack.backStack[2])

        transitionStack.stepBack()
        assertEquals(Move("", 0, 0), transitionStack.backStack[1])
        assertEquals(EndTurn(""), transitionStack.forwardStack[0])
        assertEquals(2, transitionStack.backStack.size)
        assertEquals(1, transitionStack.forwardStack.size)

        transitionStack.stepBack()
        assertEquals(StartTurn(""), transitionStack.backStack[0])
        assertEquals(Move("", 0, 0), transitionStack.forwardStack[1])
        assertEquals(1, transitionStack.backStack.size)
        assertEquals(2, transitionStack.forwardStack.size)

        transitionStack.stepBack()
        assertEquals(StartTurn(""), transitionStack.forwardStack[2])
        assertEquals(0, transitionStack.backStack.size)
        assertEquals(3, transitionStack.forwardStack.size)

        // Test that we can safely handle when the stack is empty
        transitionStack.stepBack()
        assertEquals(0, transitionStack.backStack.size)
        assertEquals(3, transitionStack.forwardStack.size)
    }

    /**
     * Test rewind entire turn
     */
    @Test
    fun `Rewind entire turn`() = runBlockingTest {
        transitionStack.executeNewTransition(StartTurn("1"))
        transitionStack.executeNewTransition(Move("1", 1, 1))
        transitionStack.executeNewTransition(EndTurn("1"))
        transitionStack.executeNewTransition(StartTurn("2"))
        transitionStack.executeNewTransition(Move("2", 2, 2))
        transitionStack.executeNewTransition(EndTurn("2"))
        assertEquals(6, transitionStack.backStack.size)

        transitionStack.rewindTurn()

        assertEquals(3, transitionStack.backStack.size)

        // Test the backstack
        assertEquals(StartTurn("1"), transitionStack.backStack[0])
        assertEquals(Move("1", 1, 1), transitionStack.backStack[1])
        assertEquals(EndTurn("1"), transitionStack.backStack[2])

        // Test the forwardStack
        assertEquals(EndTurn("2"), transitionStack.forwardStack[0])
        assertEquals(Move("2", 2, 2), transitionStack.forwardStack[1])
        assertEquals(StartTurn("2"), transitionStack.forwardStack[2])

        transitionStack.rewindTurn()
        assertEquals(0, transitionStack.backStack.size)
        assertEquals(6, transitionStack.forwardStack.size)

        // Test that we can safely handle when the stack is empty
        transitionStack.rewindTurn()
        assertEquals(0, transitionStack.backStack.size)
        assertEquals(6, transitionStack.forwardStack.size)
    }

    /**
     * Test fast forward entire turn
     */
    @Test
    fun `FastForward entire turn`() = runBlockingTest {
        transitionStack.executeNewTransition(StartTurn("1"))
        transitionStack.executeNewTransition(Move("1", 1, 1))
        transitionStack.executeNewTransition(EndTurn("1"))
        transitionStack.executeNewTransition(StartTurn("2"))
        transitionStack.executeNewTransition(Move("2", 2, 2))
        transitionStack.executeNewTransition(EndTurn("2"))

        transitionStack.rewindTurn()
        transitionStack.rewindTurn()

        assertEquals(0, transitionStack.backStack.size)
        assertEquals(6, transitionStack.forwardStack.size)

        transitionStack.fastForwardTurn()

        // Test the backstack
        assertEquals(StartTurn("1"), transitionStack.backStack[0])
        assertEquals(Move("1", 1, 1), transitionStack.backStack[1])
        assertEquals(EndTurn("1"), transitionStack.backStack[2])

        // Test the forwardStack
        assertEquals(EndTurn("2"), transitionStack.forwardStack[0])
        assertEquals(Move("2", 2, 2), transitionStack.forwardStack[1])
        assertEquals(StartTurn("2"), transitionStack.forwardStack[2])

        assertEquals(3, transitionStack.backStack.size)
        assertEquals(3, transitionStack.forwardStack.size)

        transitionStack.fastForwardTurn()

        // Test the backstack
        assertEquals(6, transitionStack.backStack.size)
        assertEquals(StartTurn("1"), transitionStack.backStack[0])
        assertEquals(Move("1", 1, 1), transitionStack.backStack[1])
        assertEquals(EndTurn("1"), transitionStack.backStack[2])
        assertEquals(StartTurn("2"), transitionStack.backStack[3])
        assertEquals(Move("2", 2, 2), transitionStack.backStack[4])
        assertEquals(EndTurn("2"), transitionStack.backStack[5])

        // Test the forwardStack
        assertTrue(transitionStack.forwardStack.isEmpty())

        // Test that we can safely handle when the stack is empty
        transitionStack.fastForwardTurn()
        assertEquals(6, transitionStack.backStack.size)
        assertTrue(transitionStack.forwardStack.isEmpty())
    }
}
