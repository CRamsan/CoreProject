package com.cramsan.ps2link.network.ws.testgui.application

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.network.ws.messages.GainExperience
import com.cramsan.ps2link.network.ws.messages.ServerEventPayloadV2
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Manage the state for a single user session. A session is considered from the moment the application starts to
 * listen for events, until we stop listening.
 */
class GameSessionManager(
    private val hotKeyManager: HotKeyManager,
    private val coroutineScope: CoroutineScope,
) {

    private var timer = 0
    private var killCounter = 0

    private var timerJob: Job? = null

    /**
     * Process a kill event.
     */
    fun onPlayerDeathEvent(death: ServerEventPayloadV2.DeathV2) {
        logI(TAG, "Kill event for character: ${death.characterId}")

        // Take screenshot right away
        hotKeyManager.executeHotKey(HotKeyType.ON_ENEMY_KILL_SCREENSHOT)

        // Add time to the timer
        timer += 10000
        killCounter += 1
        if (timerJob?.isCompleted == true || timerJob == null) {
            logI(TAG, "Launched new timer")
            timerJob = coroutineScope.launch { startTimer() }
        } else {
            logI(TAG, "Added time. New time: $timer")
        }
    }

    private suspend fun startTimer() {
        var iterations = 0
        while (timer > 0) {
            delay(1000)
            timer -= 1000
            iterations++
            if (iterations > 10) {
                logI(TAG, "Reached timer limit.")
                break
            }
            logD(TAG, "Timer tick: $timer")
        }

        if (killCounter > 3) {
            logI(TAG, "Capturing video")
            hotKeyManager.executeHotKey(HotKeyType.ON_ENEMY_KILL_VIDEO)
        } else {
            logI(TAG, "Kills: $killCounter - did not meet threshold")
        }

        timerJob = null
        timer = 0
        killCounter = 0
    }

    fun onExperienceGained(payload: GainExperience) {
        val characterId = payload.characterId
        logI(TAG, "Character: $characterId gained ${payload.amount} xp.")
    }

    companion object {
        const val TAG = "GameSessionManager"
    }
}
