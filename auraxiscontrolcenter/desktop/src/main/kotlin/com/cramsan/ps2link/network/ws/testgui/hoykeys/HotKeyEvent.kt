package com.cramsan.ps2link.network.ws.testgui.hoykeys

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

/**
 * Data class to hold the information to execute a HotKey combination.
 */
data class HotKeyEvent(
    val kotlinEvents: ImmutableList<KotlinKeyEvent>,
)

/**
 * Entity class that represents a HotKey combination.
 */
@Serializable
data class HotKeyEventEntity(
    val kotlinEvents: List<KotlinKeyEvent>,
)

/**
 * Map from the [HotKeyEvent] domain class to the [HotKeyEventEntity] entity class.
 */
fun HotKeyEvent.toEntity() = HotKeyEventEntity(
    kotlinEvents,
)

/**
 * Map from the [HotKeyEventEntity] class to a [HotKeyEvent] domain class.
 */
fun HotKeyEventEntity.toHotKeyEvent() = HotKeyEvent(
    kotlinEvents = kotlinEvents.toImmutableList(),
)
