package com.cramsan.ps2link.network.ws.testgui.hoykeys

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import kotlinx.serialization.Serializable

/**
 * Data class that represents a key event. This class is a Kotlin equivalent of [NativeKeyEvent].
 */
@Serializable
data class KotlinKeyEvent(
    val id: Int,
    val modifiers: Int,
    val rawCode: Int,
    val keyCode: Int,
    val keyChar: Int,
    val keyLocation: Int,
)

/**
 * Convert from a [NativeKeyEvent] to a [KotlinKeyEvent].
 */
fun NativeKeyEvent.toKotlinEvent() = KotlinKeyEvent(
    id = id,
    modifiers = modifiers,
    rawCode = rawCode,
    keyCode = keyCode,
    keyChar = keyChar.code,
    keyLocation = keyLocation,
)

/**
 * Convert from a [KotlinKeyEvent] to a [NativeKeyEvent].
 */
fun KotlinKeyEvent.toNativeEvent() = NativeKeyEvent(
    id,
    modifiers,
    rawCode,
    keyCode,
    keyChar.toChar(),
    keyLocation,
)
