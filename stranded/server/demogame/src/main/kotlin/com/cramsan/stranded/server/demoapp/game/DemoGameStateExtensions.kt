package com.cramsan.stranded.server.demoapp.game

/**
 * Extension class that executes any [stateChange] of type [DemoGameStateChange]. It returns a copy of the original
 * instance with the change applied.
 */
fun DemoGameState.transformWithStateChange(stateChange: DemoGameStateChange): DemoGameState {
    return when (stateChange) {
        IncrementCounter -> {
            copy(counter = counter + 1)
        }
    }
}
