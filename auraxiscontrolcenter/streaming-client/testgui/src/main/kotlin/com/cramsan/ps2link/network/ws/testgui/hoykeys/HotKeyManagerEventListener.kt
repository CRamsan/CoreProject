package com.cramsan.ps2link.network.ws.testgui.hoykeys

/**
 * Interface to be able to get events from the [HotKeyManager].
 */
interface HotKeyManagerEventListener {

    /**
     * To be called during HotKey capture. The [keyEvent] is the event from the key the user typed. [hotKeyType]
     * is the action that this hotkey will map to.
     */
    fun onKeyEvent(hotKeyType: HotKeyType, keyEvent: KotlinKeyEvent)

    /**
     * The hotkey capture was completed.
     */
    fun onCaptureComplete()
}
