package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.eventsystem.events.InteractiveEventOption

/**
 * This interface is used to allow users to select an [InteractiveEventOption] and continue execution.
 */
interface EntityManagerInteractionReceiver {
    suspend fun selectOption(option: InteractiveEventOption?)
}
