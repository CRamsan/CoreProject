package com.cramsan.ps2link.network.ws.testgui.hoykeys

import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logW
import com.cramsan.framework.preferences.Preferences
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess

/**
 * Manager class to interface with HotKeys. It allows for capturing and replaying hotkeys.
 */
class HotKeyManager(
    private val json: Json,
    private val preferences: Preferences,
) {

    private val listeners = mutableListOf<HotKeyManagerEventListener>()

    private val keyListener = HotKeyCapture()

    private val map = mutableMapOf<HotKeyType, HotKeyEvent?>()

    /**
     * Initialize this manager to be able to capture hot key events.
     */
    fun configureHotKeyManger() {
        try {
            // GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            logE(TAG, "There was a problem registering the native hook.", ex)
            exitProcess(1)
        }
    }

    /**
     * Load hotkeys from storage.
     */
    fun loadFromPreferences(): Map<HotKeyType, HotKeyEvent?> {
        HotKeyType.values().forEach {
            val value = preferences.loadString(it.name)
            val keyEvent = if (value.isNullOrBlank()) {
                null
            } else {
                json.decodeFromString<HotKeyEventEntity>(value)
            }
            map[it] = keyEvent?.toHotKeyEvent()
        }
        return map.toMap()
    }

    /**
     * Register the [eventHandler] to be notified of events.
     */
    fun registerListener(eventHandler: HotKeyManagerEventListener) {
        listeners.add(eventHandler)
    }

    /**
     * Remove the [eventHandler] from being notified of events.
     */
    fun deregisterListener(eventHandler: HotKeyManagerEventListener) {
        listeners.remove(eventHandler)
    }

    /**
     * Initiate the hotkey capture so it can be mapped to the [hotKeyType] event.
     */
    fun registerHotKeys(hotKeyType: HotKeyType) {
        keyListener.startCapture(
            object : HotKeyCaptureEventListener {
                override fun onKeyEvent(keyEvent: KotlinKeyEvent) {
                    listeners.forEach { it.onKeyEvent(hotKeyType, keyEvent) }
                }

                override fun onCaptureComplete(hotKeyEvent: HotKeyEvent) {
                    val serializedEvent = json.encodeToString(hotKeyEvent.toEntity())
                    preferences.saveString(hotKeyType.name, serializedEvent)

                    listeners.forEach { it.onCaptureComplete() }
                }
            },
        )
    }

    /**
     * Execute the stored hotkey for the events mapped to [hotKeyType].
     */
    fun executeHotKey(hotKeyType: HotKeyType) {
        val hotKeyEvent = map[hotKeyType]

        if (hotKeyEvent == null) {
            logW(TAG, "Not hotkey mapped to $hotKeyType")
            return
        }

        hotKeyEvent.kotlinEvents.forEach {
            val event = it.toNativeEvent()
            GlobalScreen.postNativeEvent(event)
        }
    }

    companion object {
        const val TAG = "HotKeyManager"
    }
}
