package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport


/**
 * Class to manage the main menu screen. It will initialize the UI components and any objects needed or the background.
 */
class MainMenuScreen : BaseScreen(), Screen {

    private var stage: Stage? = null
    private var outputLabel: Label? = null

    override fun screenInit() {
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage

        val row_height = Gdx.graphics.width / 12
        val col_width = Gdx.graphics.width / 12

        val mySkin = Skin(Gdx.files.internal("glassy-ui.json"))

        val title =
            Label("Buttons with Skins", mySkin, "big-black")
        title.setSize(Gdx.graphics.width.toFloat(), row_height * 2.toFloat())
        title.setPosition(0f, Gdx.graphics.height - row_height * 2.toFloat())
        title.setAlignment(Align.center)
        stage!!.addActor(title)

        // Button
        // Button
        val button1 = Button(mySkin, "small")
        button1.setSize((col_width * 4).toFloat(), row_height.toFloat())
        button1.setPosition(col_width.toFloat(), (Gdx.graphics.height - row_height * 3).toFloat())
        button1.addListener(object : InputListener() {
            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                outputLabel!!.setText("Press a Button")
            }

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                outputLabel!!.setText("Pressed Button")
                return true
            }
        })
        stage!!.addActor(button1)

        // Text Button
        // Text Button
        val button2: Button = TextButton("Text Button", mySkin, "small")
        button2.setSize((col_width * 4).toFloat(), row_height.toFloat())
        button2.setPosition((col_width * 7).toFloat(), (Gdx.graphics.height - row_height * 3).toFloat())
        button2.addListener(object : InputListener() {
            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                outputLabel!!.setText("Press a Button")
            }

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                outputLabel!!.setText("Pressed Text Button")
                return true
            }
        })
        stage!!.addActor(button2)


        // ImageButton
        // ImageButton
        val button3 = ImageButton(mySkin)
        button3.setSize((col_width * 4).toFloat(), (row_height * 2).toFloat())
        button3.getStyle().imageUp =
            TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("switch_off.png"))))
        button3.getStyle().imageDown =
            TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("switch_on.png"))))
        button3.setPosition(col_width.toFloat(), (Gdx.graphics.height - row_height * 6).toFloat())
        button3.addListener(object : InputListener() {
            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                outputLabel!!.setText("Press a Button")
            }

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                outputLabel!!.setText("Pressed Image Button")
                return true
            }
        })
        stage!!.addActor(button3)

        //ImageTextButton
        //ImageTextButton
        val button4 = ImageTextButton("ImageText Btn", mySkin, "small")
        button4.setSize(col_width * 4.toFloat(), (row_height * 2).toFloat())
        button4.style.imageUp =
            TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("switch_off.png"))))
        button4.style.imageDown =
            TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("switch_on.png"))))
        button4.setPosition(col_width * 7.toFloat(), Gdx.graphics.height - row_height * 6.toFloat())
        button4.addListener(object : InputListener() {
            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                outputLabel!!.setText("Press a Button")
            }

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                outputLabel!!.setText("Pressed Image Text Button")
                return true
            }
        })
        stage!!.addActor(button4)

        outputLabel = Label("Press a Button", mySkin, "black")
        outputLabel!!.setSize(Gdx.graphics.width.toFloat(), row_height.toFloat())
        outputLabel!!.setPosition(0f, row_height.toFloat())
        outputLabel!!.setAlignment(Align.center)
        stage!!.addActor(outputLabel)
    }

    override fun performCustomUpdate(delta: Float) {
    }

    override fun performRender() {
        super.performRender()
        stage?.act();
        stage?.draw();
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