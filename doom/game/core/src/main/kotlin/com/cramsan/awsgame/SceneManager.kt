package com.cramsan.awsgame

import com.cramsan.awsgame.screen.GameScreen
import com.cramsan.awsgame.screen.MainMenuScreen

object SceneManager {
    private var game: MyGdxGame? = null
    fun setGame(game: MyGdxGame?) {
        if (game == null) {
            throw RuntimeException("Game parameter is null")
        }
        if (SceneManager.game != null) {
            throw RuntimeException("Game already set")
        }
        SceneManager.game = game
    }

    fun getGame(): MyGdxGame? {
        return game
    }

    fun clearGame() {
        if (game == null) {
            throw RuntimeException("Game was not set")
        }
        game = null
    }

    private var parameterManager: GameParameterManager? = null
    fun startGameScreen(newParameterManager: GameParameterManager?) {
        if (game == null) {
            throw RuntimeException("Game parameter is null")
        }
        if (parameterManager == null) {
            parameterManager =
                newParameterManager ?: throw RuntimeException("GameParameterManager not provided.")
        } else if (newParameterManager != null) {
            throw RuntimeException("GameParameterManager already set and cannot be overridden")
        }
        val screen: GameScreen? = null //new KnightsVsNinjasScreen(parameterManager);
        game!!.screen = screen
    }

    @JvmOverloads
    fun startMainMenuScreen(isInitialLoad: Boolean = false) {
        if (game == null) {
            throw RuntimeException("Game parameter is null")
        }
        parameterManager = null
        val screen = MainMenuScreen()
        game!!.setScreen(screen, isInitialLoad)
    }
}