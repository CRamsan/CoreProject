package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.cramsan.awsgame.subsystems.AudioManager
import com.cramsan.awsgame.subsystems.CallbackManager

/**
 * Base class to handle all code shared across all scenes. This class will configure the camera, the background map
 * as well as calling the update method.
 */
abstract class BaseScreen : Screen {

    private val cam: OrthographicCamera
    private val viewport: Viewport
    var callbackManager: CallbackManager
    var audioManager: AudioManager? = null

    init {
        cam = OrthographicCamera(FPSGame.VIRTUAL_WIDTH.toFloat(), FPSGame.VIRTUAL_HEIGHT.toFloat())
        viewport = StretchViewport(cam.viewportWidth, cam.viewportHeight, cam)
        callbackManager = CallbackManager()
    }

    // This method will be called to configure objects. This is used to decouple the object initialization
    // From their configuration in the game world.
    open fun screenInit() {
        cam.setToOrtho(false)
        cam.update()
        viewport.worldWidth = cam.viewportWidth
        viewport.worldHeight = cam.viewportHeight
        audioManager!!.PlayMusic()
    }

    override fun render(delta: Float) {
        performUpdate(delta)
        performRender()
    }

    /**
     * This method will render the scene always after a fixed time step
     */
    open fun performRender() {
        viewport.apply()
        cam.update()
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
    }

    /**
     * Implement logic here that is shared for all child classes of BaseScreen.
     * You can use this method to update the callback manager and other objects
     * that are tied to the life time of a screen.
     */
    private fun performUpdate(delta: Float) {
        callbackManager.update(delta)
        performCustomUpdate(delta)
    }

    /**
     * Implement logic here that specific to each implementation of
     * this class. This method will use the provided time delta for
     * the step. This update method is called before each frame.
     */
    protected abstract fun performCustomUpdate(delta: Float)

    /**
     * This method must be implemented as a way to identify different
     * child classes. It's use is still not well defined.
     */
    protected abstract fun levelId(): Int

    override fun dispose() {}
    override fun show() {}
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
}