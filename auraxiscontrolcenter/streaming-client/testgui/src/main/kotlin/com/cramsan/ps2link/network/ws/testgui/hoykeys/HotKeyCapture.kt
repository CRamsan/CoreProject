package com.cramsan.ps2link.network.ws.testgui.hoykeys

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logI
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.collections.immutable.toImmutableList

/**
 * Instance that can listen for global key events. It will collect all key events until for a single set of events that
 * represent a hotkey event.
 */
class HotKeyCapture {

    /**
     * Start a capture
     */
    fun startCapture(listener: HotKeyCaptureEventListener) {
        logI(TAG, "Starting hotkey capture")
        NativeHotKeyListener.startCapturing(listener)
    }

    /**
     * Stop the hotkey capture. If there was a capture in progress, any listener will not get notified.
     */
    fun stopCapture() {
        logI(TAG, "Stopping hotkey capture")
        NativeHotKeyListener.stopCapturing()
    }

    private object NativeHotKeyListener : NativeKeyListener {

        var isCapturing = false
            private set

        private val commands = mutableListOf<NativeKeyEvent>()

        private var listener: HotKeyCaptureEventListener? = null

        private val pressedKeys = mutableSetOf<Int>()

        /**
         * Start to listen for new native key events.
         */
        fun startCapturing(listener: HotKeyCaptureEventListener) {
            isCapturing = true
            commands.clear()
            pressedKeys.clear()
            this.listener = listener
            GlobalScreen.addNativeKeyListener(this)
        }

        /**
         * Stop listening for native key events.
         */
        fun stopCapturing() {
            isCapturing = false
            GlobalScreen.removeNativeKeyListener(this)
        }
        override fun nativeKeyPressed(e: NativeKeyEvent) {
            if (!pressedKeys.contains(e.rawCode)) {
                pressedKeys.add(e.rawCode)
            } else {
                return
            }
            logD(TAG, "Received native event pressed: ${e.keyCode}")
            commands.add(e)

            listener?.onKeyEvent(e.toKotlinEvent())
        }
        override fun nativeKeyReleased(e: NativeKeyEvent) {
            if (pressedKeys.contains(e.rawCode)) {
                pressedKeys.remove(e.rawCode)
            } else {
                return
            }
            logD(TAG, "Received native event released: ${e.keyCode}")
            commands.add(e)

            if (isCapturing && pressedKeys.size == 0) {
                logI(TAG, "HotKey event recorded.")
                isCapturing = false
                GlobalScreen.removeNativeKeyListener(this)
                val kotlinEvents = commands.map { it.toKotlinEvent() }.toImmutableList()
                val hotKeyEvent = HotKeyEvent(
                    kotlinEvents = kotlinEvents,
                )
                listener?.onCaptureComplete(hotKeyEvent)
            }
        }
    }

    companion object {
        const val TAG = "HotKeyCapture"
    }
}
