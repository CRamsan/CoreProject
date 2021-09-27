package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.enums.DebugAction
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption

/**
 *
 */
interface EntityManagerInterface {

    fun debugAction(debugAction: DebugAction)

    fun runTurns()

    fun selectOption(option: InteractiveEventOption?)
}
