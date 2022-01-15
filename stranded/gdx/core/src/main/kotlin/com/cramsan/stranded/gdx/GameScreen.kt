package com.cramsan.stranded.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.cramsan.stranded.gdx.ui.BackgroundGdx
import com.cramsan.stranded.gdx.ui.Theme
import com.cramsan.stranded.gdx.ui.game.CraftingUIGdx
import com.cramsan.stranded.gdx.ui.game.HandUIGdx
import com.cramsan.stranded.gdx.ui.game.NightCardUIGdx
import com.cramsan.stranded.gdx.ui.game.PauseMenu
import com.cramsan.stranded.gdx.ui.game.PhaseComponentUIGdx
import com.cramsan.stranded.gdx.ui.game.PlayerHeartsUIGdx
import com.cramsan.stranded.gdx.ui.game.PlayerListUIGdx
import com.cramsan.stranded.gdx.ui.game.ShelterUIGdx
import com.cramsan.stranded.gdx.ui.game.actors.SpriteButton
import com.cramsan.stranded.lib.client.controllers.GameController
import com.cramsan.stranded.lib.client.controllers.GameControllerEventHandler
import com.cramsan.stranded.lib.client.controllers.GameMode
import com.cramsan.stranded.lib.client.ui.game.GameScreenEventHandler
import com.cramsan.stranded.lib.repository.Player
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use
import ktx.scene2d.actors
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class GameScreen(
    private val gameScreenEventHandler: GameScreenEventHandler,
) : KtxScreen {

    var controller: GameController? = null

    private val stage: Stage = stage(viewport = FitViewport(720f, 1280f))

    private val shapeRenderer = ShapeRenderer()

    val background: BackgroundGdx = BackgroundGdx(shapeRenderer)
    val pauseMenu: PauseMenu
    val playerListUI: PlayerListUIGdx
    val playerHearts: PlayerHeartsUIGdx
    val hand: HandUIGdx
    val crafting: CraftingUIGdx
    val shelterUI: ShelterUIGdx
    val phaseUI: PhaseComponentUIGdx
    val nightCardUI: NightCardUIGdx
    val readyButton: SpriteButton
    val pauseButton: SpriteButton

    init {
        // TODO: Move to the asset manager
        val basketTexture = getTextureRegion("craft_basket.png")
        val shelterTexture = getTextureRegion("craft_shelter.png")
        val spearTexture = getTextureRegion("craft_spear.png")
        val fullHeartTexture = getTextureRegion("heart_full.png")
        val emptyHeartTexture = getTextureRegion("heart_empty.png")
        val cardTexture = getTextureRegion("hand_card.png")
        val nightCardTexture = getTextureRegion("night_card.png")
        val buttonTexture = getTextureRegion("button.png")

        pauseMenu = PauseMenu()
        playerListUI = PlayerListUIGdx()
        playerHearts = PlayerHeartsUIGdx(fullHeartTexture, emptyHeartTexture)
        hand = HandUIGdx(cardTexture)
        crafting = CraftingUIGdx(basketTexture, shelterTexture, spearTexture)
        nightCardUI = NightCardUIGdx(nightCardTexture)
        shelterUI = ShelterUIGdx()
        phaseUI = PhaseComponentUIGdx()
        readyButton = SpriteButton(
            buttonTexture,
            Theme.Scale.medium,
            Theme.Scale.medium,
        ) {
            controller?.onReadyButtonPressed()
        }
        pauseButton = SpriteButton(
            buttonTexture,
            Theme.Scale.medium,
            Theme.Scale.medium,
        ) {
            controller?.setMenuMode(GameMode.Pause)
        }

        stage.actors {
            stack {
                setFillParent(true)
                table {
                    add(
                        scene2d.table {
                            padTop(Theme.Padding.xlarge) // This is the setting that manages top padding for the widget
                            addActor(phaseUI.widget)
                        }
                    ).apply {
                        top()
                        colspan(2)
                        expand()
                    }
                }
                table {
                    add(nightCardUI.widget)
                    add(hand.widget).apply {
                        expandX()
                    }
                    bottom()
                }
            }
        }

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(playerListUI.widget)
                top()
                left()
            }
        )

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(playerHearts.widget)
                bottom()
                left()
            }
        )

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(crafting.widget)
                bottom()
                left()
            }
        )

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(readyButton).apply {
                }
                bottom()
                right()
            }
        )

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(pauseMenu.widget)
                center()
            }
        )

        stage.addActor(
            scene2d.table {
                setFillParent(true)
                add(pauseButton).apply {
                }
                top()
                right()
            }
        )

        stage.isDebugAll = true
    }

    fun configureGame(playerId: String, playerList: List<Player>, lobbyId: String) {
        controller?.configureController(
            playerId,
            playerList,
            lobbyId,
            playerListUI,
            playerHearts,
            hand,
            shelterUI,
            phaseUI,
            nightCardUI,
            crafting,
            pauseMenu,
            object : GameControllerEventHandler {
                override fun onExitGameSelected() {
                    gameScreenEventHandler.onGameEnd()
                }
            },
            background,
        )

        controller?.let {
            crafting.eventHandler = it
            hand.eventHandler = it
            pauseMenu.eventHandler = it
        }
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
        controller?.onShow()
    }

    override fun render(delta: Float) {
        clearScreen(0.1f, 0.1f, 0.1f)

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
        controller?.onDispose()
        stage.dispose()
    }

    fun getTextureRegion(asset: String): TextureRegion {
        val texture = Texture(Gdx.files.internal(asset))
        return TextureRegion(texture, 0, 0, texture.width, texture.height)
    }
}
