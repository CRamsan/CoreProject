package com.cramsan.awslib.entitymanager.implementation

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType

class TurnAction(
    override val turnActionType: TurnActionType,
    override val direction: Direction
) : TurnActionInterface {

    override fun toString(): String {
        return "Type: $turnActionType, Direction: $direction"
    }

    companion object {
        val NOOP: TurnActionInterface = TurnAction(TurnActionType.NONE, Direction.KEEP)
    }
}
