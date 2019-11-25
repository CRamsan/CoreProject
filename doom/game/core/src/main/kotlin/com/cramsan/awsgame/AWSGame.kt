package com.cramsan.awsgame

import com.badlogic.gdx.ApplicationAdapter

class AWSGame : ApplicationAdapter() {

    private var player: Player? = null
    private var map: Map? = null
    private var controls: Controls? = null
    private var camera: Camera? = null

    private var seconds = 0f
    private var viewport: com.badlogic.gdx.math.Rectangle? = null
    private var scale = 1f
    private var orthoCamera: com.badlogic.gdx.graphics.OrthographicCamera? = null

    override fun create() {
        // Setup 2d camera with top left coordinates
        // http://stackoverflow.com/questions/7708379/changing-the-coordinate-system-in-libgdx-java/7751183#7751183
        // This forces us to flip textures on the y axis, eg. in Camera#drawSky
        orthoCamera = com.badlogic.gdx.graphics.OrthographicCamera(VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())
        orthoCamera!!.setToOrtho(true, VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())

        this.player = Player(15.3, -1.2, Math.PI * 0.3)
        this.map = Map(32)
        this.controls = Controls()
        this.camera = Camera(orthoCamera!!, 320.0, Math.PI * 0.4)

        this.map!!.randomize()
    }

    override fun resize(width: Int, height: Int) {
        // calculate new viewport
        val aspectRatio = width.toFloat() / height.toFloat()

        val crop = com.badlogic.gdx.math.Vector2(0f, 0f)
        if (aspectRatio > ASPECT_RATIO) {
            scale = height.toFloat() / VIRTUAL_HEIGHT.toFloat()
            crop.x = (width - VIRTUAL_WIDTH * scale) / 2f
        } else if (aspectRatio < ASPECT_RATIO) {
            scale = width.toFloat() / VIRTUAL_WIDTH.toFloat()
            crop.y = (height - VIRTUAL_HEIGHT * scale) / 2f
        } else {
            scale = width.toFloat() / VIRTUAL_WIDTH.toFloat()
        }

        val w = VIRTUAL_WIDTH.toFloat() * scale
        val h = VIRTUAL_HEIGHT.toFloat() * scale
        viewport = com.badlogic.gdx.math.Rectangle(crop.x, crop.y, w, h)
    }

    override fun render() {
        if (com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            com.badlogic.gdx.Gdx.app.exit()
        }

        com.badlogic.gdx.Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        com.badlogic.gdx.Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        orthoCamera!!.update()
        com.badlogic.gdx.Gdx.gl.glViewport(viewport!!.x.toInt(), viewport!!.y.toInt(), viewport!!.width.toInt(), viewport!!.height.toInt())

        seconds = com.badlogic.gdx.Gdx.graphics.deltaTime

        map!!.update(seconds.toDouble())
        controls!!.update()
        player!!.update(controls!!, map!!, seconds.toDouble())
        camera!!.render(player!!, map!!)
    }

    companion object {
        val CIRCLE = Math.PI * 2

        private val VIRTUAL_WIDTH = 1024
        private val VIRTUAL_HEIGHT = 640
        private val ASPECT_RATIO = VIRTUAL_WIDTH.toFloat() / VIRTUAL_HEIGHT.toFloat()
    }
}
