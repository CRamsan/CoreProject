package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.cramsan.awsgame.GameParameterManager
import com.cramsan.awsgame.SceneManager

/**
 * Class to manage the main menu screen. It will initialize the UI components and any objects needed or the background.
 */
class MainMenuScreen : BaseScreen(), Screen {

    private var stage: Stage? = null
    private var batch: SpriteBatch? = null
    private var background: Texture? = null

    override fun screenInit() {
        super.screenInit()

        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage

        background = Texture(Gdx.files.internal("main_menu.jpg"))
        batch = SpriteBatch()

        val mySkin = Skin(Gdx.files.internal("skin/star-soldier-ui.json"))

        val parentTable = Table(mySkin)
        val mainPane = Table()
        mainPane.setFillParent(true)
        mainPane.add(parentTable)

        val startGameButton = TextButton("Start", mySkin)
        startGameButton.addListener(
            object : ChangeListener() {
                override fun changed(
                    event: ChangeEvent,
                    actor: Actor
                ) {
                    SceneManager.startGameScreen(GameParameterManager())
                }
            }
        )
        parentTable.add(startGameButton).row()

        stage!!.addActor(mainPane)
        stage!!.isDebugAll = true
    }

    override fun performCustomUpdate(delta: Float) {
    }

    override fun performRender() {
        super.performRender()

        viewport.apply()
        cam.update()

        batch!!.projectionMatrix = viewport.camera.combined
        batch!!.begin()
        batch!!.draw(background, 0F, 0F, viewport.worldWidth, viewport.worldHeight)
        batch!!.end()

        stage!!.viewport.apply()
        stage?.act()
        stage!!.draw()
    }

    override fun dispose() {
        super.dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun levelId(): Int {
        return 0
    }
}
