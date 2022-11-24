package com.cramsan.ps2link.network.ws.testgui.hoykeys

/**
 * Callback for events from the [HotKeyCapture].
 */
interface HotKeyCaptureEventListener {

    /**
     * Called when the user pressed on a key.
     */
    fun onKeyEvent(keyEvent: KotlinKeyEvent)

    /**
     * Called when the hotkey capture process completes. The [hotKeyEvent] holds the captured hotkey.
     */
    fun onCaptureComplete(hotKeyEvent: HotKeyEvent)
}
