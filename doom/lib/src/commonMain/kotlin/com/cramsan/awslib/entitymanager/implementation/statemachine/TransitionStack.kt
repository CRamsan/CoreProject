package com.cramsan.awslib.entitymanager.implementation.statemachine

import com.cramsan.framework.logging.logD

class TransitionStack(private val transitionHandler: TransitionHandler) {

    internal val backStack = mutableListOf<Transition>()

    internal val forwardStack = mutableListOf<Transition>()

    fun executeNewTransition(transition: Transition) {
        logD(TAG, "Executing transition: $transition")
        // If executing a new transition, clear the forward stack.
        forwardStack.clear()
        backStack.add(transition)
        transitionHandler.executeTransition(transition)
    }

    fun stepBack() {
        if (backStack.isEmpty()) {
            logD(TAG, "Backstack is empty")
            return
        }
        val lastTransition = backStack.removeLast()
        logD(TAG, "Stepping back one state: $lastTransition")
        forwardStack.add(lastTransition)
        transitionHandler.executeTransition(lastTransition.toReverse())
        logD(TAG, "New last state: ${backStack.lastOrNull()}")
    }

    fun stepForward() {
        if (forwardStack.isEmpty()) {
            logD(TAG, "Forward stack is empty")
            return
        }
        val lastTransition = forwardStack.removeLast()
        logD(TAG, "Stepping forward one state: $lastTransition")
        backStack.add(lastTransition)
        transitionHandler.executeTransition(lastTransition)
        logD(TAG, "Next available state: ${forwardStack.lastOrNull()}")
    }

    fun rewindTurn() {
        logD(TAG, "Rewinding entire turn")
        while (backStack.isNotEmpty()) {
            stepBack()
            if (backStack.isEmpty() || backStack.last() is EndTurn) {
                logD(TAG, "Completed rewinding turn")
                break
            }
        }
    }

    fun fastForwardTurn() {
        logD(TAG, "FastForward entire turn")
        while (forwardStack.isNotEmpty()) {
            val shouldStop = forwardStack.last() is EndTurn
            stepForward()
            if (shouldStop) {
                logD(TAG, "Completed fastforwarding turn")
                break
            }
        }
    }

    interface TransitionHandler {
        fun executeTransition(transition: Transition)
    }

    companion object {
        const val TAG = "TransitionStack"
    }
}
