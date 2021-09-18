package com.cramsan.awsgame

import com.cramsan.awsgame.screen.FPSGame
import com.cramsan.awsgame.screen.GameScreen
import com.cramsan.awsgame.screen.MainMenuScreen
import kotlinx.coroutines.DelicateCoroutinesApi

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

    private var parameterHolder: GameParameterManager? = null
    @OptIn(DelicateCoroutinesApi::class)
    fun startGameScreen(newParameterHolder: GameParameterManager?) {
        if (game == null) {
            throw RuntimeException("Game parameter is null")
        }
        if (parameterHolder == null) {
            parameterHolder =
                newParameterHolder ?: throw RuntimeException("GameParameterManager not provided.")
        } else if (newParameterHolder != null) {
            throw RuntimeException("GameParameterManager already set and cannot be overridden")
        }
        val screen: GameScreen = FPSGame()
        game!!.setScreen(screen)
    }

    @JvmOverloads
    fun startMainMenuScreen(isInitialLoad: Boolean = false) {
        if (game == null) {
            throw RuntimeException("Game parameter is null")
        }
        parameterHolder = null
        val screen = MainMenuScreen()
        game!!.setScreen(screen, isInitialLoad)
    }
}
