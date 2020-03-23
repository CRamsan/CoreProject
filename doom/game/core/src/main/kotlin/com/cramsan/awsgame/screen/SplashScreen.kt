package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class SplashScreen(private val minLoadTime: Float, private val onComplete: IResourcesLoaded) : Screen {
    var isLoadCompleted = false
    private var counter = 0f

    private var texture: Texture
    private val viewport: Viewport
    private val batch: SpriteBatch
    private val stage: Stage

    interface IResourcesLoaded {
        fun onResourcesLoaded()
    }

    init {
        texture = Texture(Gdx.files.internal("badlogic.jpg"))
        val textureWith = texture.width.toFloat()
        val textureHeight = texture.height.toFloat()
        viewport = FitViewport(textureWith, textureHeight)
        batch = SpriteBatch()
        stage = Stage(viewport)
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (isLoadCompleted && counter > minLoadTime) {
            onComplete.onResourcesLoaded()
        } else {
            counter += delta
            Gdx.gl.glClearColor(0F, 0F, 0F, 0F)
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
            viewport.apply()
            batch.projectionMatrix = viewport.camera.combined
            batch.begin()
            batch.draw(texture, 0F, 0F, viewport.worldWidth, viewport.worldHeight)
            batch.end()
            stage.viewport.apply()
            stage.draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
