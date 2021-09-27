package com.cramsan.awslib.scene

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.enums.DebugAction
import com.cramsan.framework.logging.EventLoggerInterface

class Scene(
    private val entityManager: EntityManager,
    private val sceneConfig: SceneConfig,
    private val log: EventLoggerInterface
) {

    private val mainPlayer = sceneConfig.player
    private var isLoaded = false
    private val tag = "Scene"

    fun loadScene(listener: SceneEventsCallback? = null) {
        log.i(tag, "Loading mainPlayer: $mainPlayer")
        entityManager.register(mainPlayer)
        sceneConfig.characterList.forEach {
            log.i(tag, "Registering entity: $it")
            entityManager.register(it)
        }
        entityManager.callback = listener
        isLoaded = true
    }

    fun debugAction(action: DebugAction) {
        log.d(tag, "")
        log.i(tag, "Debug Action: $action")
        entityManager.debugAction(action)
        log.i(tag, "Debug Action completed")
        log.d(tag, "")
    }

    fun runTurn(turnAction: TurnActionInterface) {
        if (!isLoaded) {
            throw RuntimeException("Trying to play game but scene is not loaded")
        }

        log.d(tag, "")
        log.i(tag, "Starting Turn")
        log.i(tag, "Turn Action: $turnAction")
        mainPlayer.nextTurnAction = turnAction
        entityManager.runTurns()
        entityManager.processGameEntityState()
        log.i(tag, "Turn completed")
        log.d(tag, "")
    }
}
