package com.cramsan.awslib.entitymanager.implementation.statemachine

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.KeyItem
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.SwapCharacterEvent

/**
 * Base class that all transitions inherit from. The purpose of the [id] is defined by the
 * implementation. The classes that implement [Transition] will need to have all the information
 * to execute the transition and to be able to create it's inverse as well.
 */
sealed class Transition(open val id: String) {
    /**
     * For the purpose of being able to rewind a transition, this function will produce the inverse
     * of the current transition.
     */
    abstract fun toReverse(): Transition
}
/*
data class CreateCharacter(
    override val id: String,
    val template: String,
    val group: String,
    val posX: Int,
    val posY: Int,
    val priority: Int,
    val enabled: Boolean,
) : Transition(id) {
    override fun toReverse() = DestroyCharacter(id, template, group, posX, posY, priority, enabled)
}

data class CreateItem(
    override val id: String,
    val template: String,
    val posX: Int,
    val posY: Int,
) : Transition(id) {
    override fun toReverse() = DestroyItem(id, template, posX, posY)
}

data class DestroyCharacter(
    override val id: String,
    val template: String,
    val group: String,
    val posX: Int,
    val posY: Int,
    val priority: Int,
    val enabled: Boolean,
) : Transition(id) {
    override fun toReverse() = CreateCharacter(id, template, group, posX, posY, priority, enabled)
}

data class DestroyItem(
    override val id: String,
    val template: String,
    val posX: Int,
    val posY: Int,
) : Transition(id) {
    override fun toReverse() = CreateItem(id, template, posX, posY)
}
 */

data class Enable(override val id: String, val target: CharacterInterface) : Transition(id) {
    override fun toReverse() = Disable(id, target)
}

data class Disable(override val id: String, val target: CharacterInterface) : Transition(id) {
    override fun toReverse() = Enable(id, target)
}

data class Move(
    override val id: String,
    val dx: Int,
    val dy: Int,
) : Transition(id) {
    override fun toReverse() = Move(id, -dx, -dy)
}

data class StartTurn(override val id: String) : Transition(id) {
    override fun toReverse() = EndTurn(id)
}

data class EndTurn(override val id: String) : Transition(id) {
    override fun toReverse() = StartTurn(id)
}

data class CloseDoor(override val id: String, val posX: Int, val posY: Int) : Transition(id) {
    override fun toReverse() = OpenDoor(id, posX, posY)
}

data class OpenDoor(override val id: String, val posX: Int, val posY: Int) : Transition(id) {
    override fun toReverse() = CloseDoor(id, posX, posY)
}

data class ConsumeItem(override val id: String, val consumableItem: ConsumableItem) :
    Transition(id) {
    override fun toReverse() = ReconstructItem(id, consumableItem)
}

data class ReconstructItem(override val id: String, val consumableItem: ConsumableItem) :
    Transition(id) {
    override fun toReverse() = ConsumeItem(id, consumableItem)
}

data class PickUpItem(override val id: String, val consumableItem: KeyItem) : Transition(id) {
    override fun toReverse() = DropItem(id, consumableItem)
}

data class DropItem(override val id: String, val consumableItem: KeyItem) : Transition(id) {
    override fun toReverse() = PickUpItem(id, consumableItem)
}

data class EquipItem(override val id: String, val consumableItem: EquippableItem) : Transition(id) {
    override fun toReverse() = UnequiptItem(id, consumableItem)
}

data class UnequiptItem(override val id: String, val consumableItem: EquippableItem) :
    Transition(id) {
    override fun toReverse() = EquipItem(id, consumableItem)
}

data class SwapCharacter(
    override val id: String,
    val originEventId: String,
    val event: SwapCharacterEvent,
) : Transition(id) {
    override fun toReverse() = SwapCharacter(
        id,
        event.nextEventId,
        event.copy(
            enableId = event.disableId,
            disableId = event.enableId,
            nextEventId = originEventId,
        ),
    )
}

data class DisplayInteractiveEvent(override val id: String, val event: InteractiveEvent) : Transition(id) {
    override fun toReverse() = HideInteractiveEvent(
        id,
        event,
    )
}

data class HideInteractiveEvent(override val id: String, val originEvent: InteractiveEvent) : Transition(id) {
    override fun toReverse() = DisplayInteractiveEvent(
        id,
        originEvent,
    )
}

class DamageEntity(override val id: String, val target: CharacterInterface, val damage: Int) : Transition(id) {
    override fun toReverse() = DamageEntity(id, target, damage * -1)
}
