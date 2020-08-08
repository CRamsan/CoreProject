package com.cramsan.awsgame

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cramsan.awsgame.SceneManager.startMainMenuScreen
import com.cramsan.awsgame.screen.BaseScreen
import com.cramsan.awsgame.screen.SplashScreen
import com.cramsan.awsgame.screen.SplashScreen.IResourcesLoaded
import com.cramsan.awsgame.subsystems.AudioManager
import com.cramsan.awsgame.subsystems.CallbackManager
import com.cramsan.awsgame.subsystems.IGameSubsystem

class MyGdxGame : Game() {
    var isUseFixedStep = true
    var isEnableRender = true
    var isEnableGame = true
    private var timeBuffer = 0f
    var isSkipSplashScreen = false
    private val listener: IGameStateListener? = null
    // Game subsystems. All this classes implement IGameSubsystem to make them easier to
    // manage and mock.
    var audioManager: AudioManager? = null
    var callbackManager: CallbackManager? = null
    private var splashScreen: SplashScreen? = null
    var spriteBatch: SpriteBatch? = null
    private val subsystemList: ArrayList<IGameSubsystem?>
    override fun create() {
        if (!isSkipSplashScreen) {
            splashScreen = SplashScreen(
                1f,
                object : IResourcesLoaded {
                    override fun onResourcesLoaded() {
                        SceneManager.startGameScreen(GameParameterManager())
                        // startMainMenuScreen(true)
                    }
                }
            )
            this.setScreen(splashScreen)
        }
        SceneManager.setGame(this)
        if (spriteBatch == null) {
            spriteBatch = SpriteBatch()
        }
        if (audioManager == null) {
            audioManager = AudioManager()
        }
        subsystemList.add(audioManager)
        if (callbackManager == null) {
            callbackManager = CallbackManager()
        }
        subsystemList.add(callbackManager)
        for (subsystem in subsystemList) {
            subsystem!!.OnGameLoad()
        }
        // Only load the main menu if the enableGame is true and we are skipping
        // the splash screen. If splashScreen is in use, then the main menu will be delegated
        // to it.
        if (isEnableGame) {
            if (isSkipSplashScreen) startMainMenuScreen() else splashScreen!!.isLoadCompleted = true
        }
        listener?.onGameCreated()
    }

    fun setScreen(newScreen: BaseScreen) {
        this.setScreen(newScreen, true)
    }

    fun setScreen(
        newScreen: BaseScreen,
        isInitialLoad: Boolean
    ) { // Check if there was an existing screen and if there was hide it.
        if (screen != null) {
            if (!isInitialLoad) {
                for (subsystem in subsystemList) {
                    subsystem!!.OnScreenClose()
                }
            }
            screen.hide()
        }
        // Prepare the new screen
        newScreen.audioManager = audioManager
        newScreen.callbackManager = callbackManager!!
        for (subsystem in subsystemList) {
            subsystem!!.OnScreenLoad()
        }
        newScreen.screenInit()
        // Set the screen and show it.
        screen = newScreen
        screen.show()
        screen.resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun resume() {
        super.resume()
    }

    override fun render() {
        if (!isEnableRender || screen == null) return
        if (isUseFixedStep) {
            timeBuffer += Gdx.graphics.deltaTime
            while (timeBuffer > FRAME_TIME) {
                screen.render(FRAME_TIME)
                timeBuffer -= FRAME_TIME
            }
        } else {
            screen.render(Gdx.graphics.deltaTime)
        }
    }

    override fun pause() {
        super.pause()
    }

    override fun dispose() {
        super.dispose()
        for (subsystem in subsystemList) {
            subsystem!!.OnGameClose()
        }
        SceneManager.clearGame()
        listener?.onGameDestroyed()
    }

    companion object {
        private const val FRAME_TIME = 1f / 60f
    }

    init {
        subsystemList = ArrayList()
    }
}
