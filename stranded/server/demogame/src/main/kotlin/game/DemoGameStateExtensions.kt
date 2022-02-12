package game

import com.cramsan.stranded.server.demoapp.game.DemoGameState
import com.cramsan.stranded.server.demoapp.game.DemoGameStateChange
import com.cramsan.stranded.server.demoapp.game.IncrementCounter

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
