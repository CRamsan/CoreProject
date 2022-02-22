package com.cramsan.stranded.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.cramsan.stranded.gdx.ui.BackgroundGdx
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.Dispatchers
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.scene2d.actors
import ktx.scene2d.stack

class MainMenuScreen(
    private val cardRepository: CardRepository,
) : KtxScreen {

    private val stage: Stage = stage(viewport = ScreenViewport())

    private lateinit var controller: GameController

    private val shapeRenderer = ShapeRenderer()

    val background: BackgroundGdx = BackgroundGdx(shapeRenderer)

    init {
        stage.actors {
            stack {
                setFillParent(true)
            }
        }
        stage.isDebugAll = true
    }

    override fun show() {
        super.show()
        controller = GameController(
            cardRepository,
            Dispatchers.Main,
        )
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
}
