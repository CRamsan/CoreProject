package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.scene.SceneEventsCallback

/**
 *
 */
interface EntityManagerInterface {

    suspend fun runTurns(callback: SceneEventsCallback?)

    suspend fun selectOption(option: InteractiveEventOption?)
}
