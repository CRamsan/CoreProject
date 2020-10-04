package com.cramsan.awslib.dsl

import com.cramsan.awslib.eventsystem.CharacterTrigger
import com.cramsan.awslib.eventsystem.triggers.CellTrigger
import com.cramsan.awslib.eventsystem.triggers.Trigger
import com.cramsan.awslib.utils.constants.InitialValues

class CellTriggerBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_TRIGGER
    var posX = InitialValues.POS_X_TRIGGER
    var posY = InitialValues.POS_Y_TRIGGER

    internal fun build() = CellTrigger(
        id,
        eventId,
        enabled,
        posX,
        posY
    )
}

class CharacterTriggerBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_TRIGGER
    var targetId = InitialValues.INVALID_ID

    internal fun build() = CharacterTrigger(
        id,
        eventId,
        enabled,
        targetId
    )
}

class TriggerListBuilder {
    private val triggerList = mutableListOf<Trigger>()

    fun cell(block: CellTriggerBuilder.() -> Unit) {
        triggerList.add(CellTriggerBuilder().apply(block).build())
    }

    fun character(block: CharacterTriggerBuilder.() -> Unit) {
        triggerList.add(CharacterTriggerBuilder().apply(block).build())
    }

    internal fun build() = triggerList
}
