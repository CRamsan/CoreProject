package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType

interface TurnActionInterface {
    val turnActionType: TurnActionType
    val direction: Direction
}
