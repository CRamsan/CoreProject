package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.eventsystem.events.InteractiveEventOption

interface EntityManagerEventListener {

    fun onGameReady(eventReceiver: EntityManagerInteractionReceiver)

    fun onInteractionRequired(text: String, options: List<InteractiveEventOption>, eventReceiver: EntityManagerInteractionReceiver)

    fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver)
}
