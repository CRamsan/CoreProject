package com.cramsan.awslib.scene

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.framework.logging.EventLoggerInterface

class Scene(
    private val entityManager: EntityManager,
    private val sceneConfig: SceneConfig,
    private val log: EventLoggerInterface
) {

    private val mainPlayer = sceneConfig.player
    private var callback: SceneEventsCallback? = null
    private var isLoaded = false
    private val tag = "Scene"

    fun loadScene() {
        log.i(tag, "Loading mainPlayer: $mainPlayer")
        entityManager.register(mainPlayer)
        sceneConfig.characterList.forEach {
            log.i(tag, "Registering entity: $it")
            if (!entityManager.register(it)) {
                log.e(tag, "Could not register: $it")
            }
        }

        isLoaded = true
    }

    suspend fun runTurn(turnAction: TurnActionInterface) {
        if (!isLoaded) {
            throw RuntimeException("Trying to play game but scene is not loaded")
        }

        log.i(tag, "Turn Action: $turnAction")
        mainPlayer.nextTurnAction = turnAction
        entityManager.runTurns(callback)
        entityManager.processGameEntityState()
    }

    fun setListener(listener: SceneEventsCallback) {
        callback = listener
    }
}
