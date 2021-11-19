package com.cramsan.stranded.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.cramsan.stranded.lib.JvmClient
import com.cramsan.stranded.lib.Server
import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.controllers.DebugGameController
import com.cramsan.stranded.lib.client.controllers.DefaultGameController
import com.cramsan.stranded.lib.client.ui.game.GameScreenEventHandler
import com.cramsan.stranded.lib.repository.GameScope
import com.cramsan.stranded.lib.repository.Player
import ktx.app.KtxGame
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class StrandedGame : KtxGame<Screen>(null) {

    private val server: Server = Server()

    private val client: Client = JvmClient()

    override fun create() {
        super.create()
        KtxAsync.initiate()
        server.start()
        client.start()

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("uiskin.json"))
        addScreen(
            MainMenuScreen::class.java,
            MainMenuScreen(
                client,
                object : MainMenuScreen.EventHandler {
                    override fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String) {
                        val gameScreen = getScreen<GameScreen>()
                        gameScreen.controller = DefaultGameController(client, KtxAsync, GameScope(KtxAsync.coroutineContext))
                        gameScreen.configureGame(playerId, playerList, lobbyId)
                        setScreen<GameScreen>()
                    }

                    override fun startDebugGameScreen() {
                        val gameScreen = getScreen<GameScreen>()
                        gameScreen.controller = DebugGameController(GameScope(KtxAsync.coroutineContext))
                        gameScreen.configureGame("", listOf(), "")
                        setScreen<GameScreen>()
                    }
                }
            )
        )
        addScreen(
            GameScreen::class.java,
            GameScreen(object : GameScreenEventHandler {
                override fun onGameEnd() {
                    setScreen<MainMenuScreen>()
                }
            })
        )
        setScreen<MainMenuScreen>()
    }
}
