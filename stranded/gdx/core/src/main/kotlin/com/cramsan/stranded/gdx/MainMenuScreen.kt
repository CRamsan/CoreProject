package com.cramsan.stranded.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.cramsan.stranded.gdx.ui.BackgroundGdx
import com.cramsan.stranded.gdx.ui.mainmenu.CreateLobbyMenu
import com.cramsan.stranded.gdx.ui.mainmenu.LobbyListMenuGdx
import com.cramsan.stranded.gdx.ui.mainmenu.LobbyMenuGdx
import com.cramsan.stranded.gdx.ui.mainmenu.LobbyPlayerListUIGdx
import com.cramsan.stranded.gdx.ui.mainmenu.MainMenu
import com.cramsan.stranded.gdx.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.controllers.DefaultMainMenuController
import com.cramsan.stranded.lib.client.controllers.MainMenuEventHandler
import com.cramsan.stranded.lib.repository.Player
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.graphics.use
import ktx.scene2d.actors
import ktx.scene2d.stack

class MainMenuScreen(
    client: Client,
    val eventHandler: EventHandler,
) : KtxScreen {

    private val stage: Stage = stage(viewport = ScreenViewport())

    private var controller: DefaultMainMenuController

    private val shapeRenderer = ShapeRenderer()

    val background: BackgroundGdx = BackgroundGdx(shapeRenderer)

    init {
        val playerNameMenu = PlayerNameMenu()

        val mainMenu = MainMenu()

        val createLobbyMenu = CreateLobbyMenu()

        val lobbyListMenu = LobbyListMenuGdx()

        val lobbyPlayerList = LobbyPlayerListUIGdx()

        val lobbyMenu = LobbyMenuGdx(client.player.id, lobbyPlayerList)

        stage.actors {
            stack {
                setFillParent(true)
                add(mainMenu.widget)
                add(playerNameMenu.widget)
                add(createLobbyMenu.widget)
                add(lobbyListMenu.widget)
                add(lobbyMenu.widget)
            }
        }
        stage.isDebugAll = true

        controller = DefaultMainMenuController(
            client,
            KtxAsync,
            playerNameMenu,
            mainMenu,
            createLobbyMenu,
            lobbyListMenu,
            lobbyMenu,
            object : MainMenuEventHandler {
                override fun onExitSelected() {
                    Gdx.app.exit()
                }

                override fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String) {
                    eventHandler.onGameStarted(playerId, playerList, lobbyId)
                }

                override fun onDebugGameScreenSelected() {
                    eventHandler.startDebugGameScreen()
                }
            },
        )

        createLobbyMenu.eventHandler = controller
        lobbyListMenu.eventHandler = controller
        lobbyMenu.eventHandler = controller
        playerNameMenu.eventHandler = controller
        mainMenu.controller = controller
    }

    override fun show() {
        super.show()
        controller.onShow()
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        // Draw background
        background.act(delta)
        background.draw(stage.width, stage.height, stage.camera)

        stage.act(delta)
        stage.draw()
        stage.batch.use {
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        controller.onDispose()
        stage.dispose()
    }

    interface EventHandler {

        fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String)

        fun startDebugGameScreen()
    }
}
