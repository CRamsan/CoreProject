package com.cramsan.awslib.scene

import com.cramsan.awslib.entitymanager.TurnActionInterface
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.utils.logging.Logger
import com.cramsan.awslib.utils.logging.Severity

class Scene(private val entityManager: EntityManager, private val sceneConfig: SceneConfig) {

    private val mainPlayer = sceneConfig.player
    private var callback: SceneEventsCallback? = null
    private var isLoaded = false

    fun loadScene() {
        entityManager.register(mainPlayer)
        sceneConfig.entityList.forEach {
            if (!entityManager.register(it)) {
                Logger.log(Severity.ERROR, "Could not register dog $it")
            }
        }
        isLoaded = true
    }

    suspend fun runTurn(turnAction: TurnActionInterface) {
        if (!isLoaded) {
            return
        }

        mainPlayer.nextTurnAction = turnAction
        entityManager.runTurns(callback)
        entityManager.processGameEntityState()
    }

    fun setListener(listener: SceneEventsCallback) {
        callback = listener
    }
}
