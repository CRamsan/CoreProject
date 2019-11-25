package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.SwapEntityEvent
import com.cramsan.awslib.scene.SceneEventsCallback

/**
 *
 */
interface EntityManagerInterface {

    fun register(entity: GameEntityInterface): Boolean
    fun deregister(entity: GameEntityInterface): Boolean
    fun setEntityState(entity: GameEntityInterface, enabled: Boolean)
    fun prepareForAdd(entity: GameEntityInterface)
    fun prepareForRemove(entity: GameEntityInterface)

    suspend fun runTurns(callback: SceneEventsCallback?)
    fun handleSwapEntityEvent(event: SwapEntityEvent, callback: SceneEventsCallback?): BaseEvent?
    fun handleChangeTriggerEvent(event: ChangeTriggerEvent): BaseEvent?
    suspend fun selectOption(option: InteractiveEventOption?)
    fun processGameEntityState()

    fun setPosition(entity: GameEntityInterface, poxX: Int, posY: Int): Boolean
    fun doDamage(attacker: GameEntityInterface, defender: GameEntityInterface, callback: SceneEventsCallback?)
    fun getEntity(posX: Int, posY: Int): GameEntityInterface?
    fun isBlocked(posX: Int, posY: Int): Boolean
}