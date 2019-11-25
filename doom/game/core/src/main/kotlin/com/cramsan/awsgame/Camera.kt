package com.cramsan.awsgame

class Camera(private val camera: com.badlogic.gdx.graphics.OrthographicCamera, protected var resolution: Double, protected var fov: Double) {
    protected var width: Double = 0.toDouble()
    protected var height: Double = 0.toDouble()
    protected var spacing: Double = 0.toDouble()
    protected var range: Double = 0.toDouble()
    protected var lightRange: Double = 0.toDouble()
    protected var scale: Double = 0.toDouble()
    private val batch: com.badlogic.gdx.graphics.g2d.SpriteBatch
    private val shapeRenderer: com.badlogic.gdx.graphics.glutils.ShapeRenderer

    init {
        this.batch = com.badlogic.gdx.graphics.g2d.SpriteBatch()
        this.batch.projectionMatrix = camera.combined
        this.shapeRenderer = com.badlogic.gdx.graphics.glutils.ShapeRenderer()
        this.shapeRenderer.projectionMatrix = camera.combined
        this.width = this.camera.viewportWidth.toDouble()
        this.height = this.camera.viewportHeight.toDouble()
        this.spacing = this.width / resolution
        this.range = 14.0
        this.lightRange = 5.0
        this.scale = (this.width + this.height) / 1200
    }

    fun render(player: Player, map: Map) {
        this.drawSky(player.direction, map.skybox, map.light)
        this.drawColumns(player, map)
        this.drawWeapon(player.weapon, player.paces)
    }

    private fun drawSky(direction: Double, sky: com.badlogic.gdx.graphics.Texture, ambient: Double) {
        val width = this.width * (AWSGame.CIRCLE / this.fov)
        val left = -width * direction / AWSGame.CIRCLE

        batch.begin()
        batch.draw(sky, left.toFloat(), 0.toFloat(), width.toFloat(), this.height.toFloat(), 0, 0, sky.width, sky.height, false, true)
        if (left < width - this.width) {
            batch.draw(sky, (left + width).toFloat(), 0.toFloat(), width.toFloat(), this.height.toFloat(), 0, 0, sky.width, sky.height, false, true)
        }
        batch.end()

        if (ambient > 0) {
            com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND)
            com.badlogic.gdx.Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA)
            shapeRenderer.setColor(1f, 1f, 1f, (ambient * 0.1).toFloat())
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled)
            shapeRenderer.rect(0f, 0f, this.width.toFloat(), (this.height * 0.5).toFloat())
            shapeRenderer.end()
            com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND)
        }
    }

    private fun drawColumns(player: Player, map: Map) {
        var column = 0
        while (column < this.resolution) {
            val angle = this.fov * (column / this.resolution - 0.5)
            val ray = map.cast(player.toPoint(), player.direction + angle, this.range)
            this.drawColumn(column.toDouble(), ray, angle, map)
            column++
        }
    }

    private fun drawWeapon(weapon: com.badlogic.gdx.graphics.Texture, paces: Double) {
        val bobX = Math.cos(paces * 2) * this.scale * 6.0
        val bobY = Math.sin(paces * 4) * this.scale * 6.0
        val left = this.width * 0.66 + bobX
        val top = this.height * 0.6 + bobY
        batch.begin()
        batch.draw(weapon, left.toFloat(), top.toFloat(), (weapon.width * this.scale).toFloat(), (weapon.height * this.scale).toFloat(), 0, 0, weapon.width, weapon.height, false, true)
        batch.end()
    }

    private fun drawColumn(column: Double, ray: Ray, angle: Double, map: Map) {
        val texture = map.wallTexture
        val left = Math.floor(column * this.spacing)
        val width = Math.ceil(this.spacing)
        var hit = -1

        while (++hit < ray.steps.size && ray.steps[hit].height <= 0);

        for (s in ray.steps.indices.reversed()) {
            val step = ray.steps[s]
            var rainDrops = Math.pow(Math.random(), 3.0) * s
            var rain: Projection? = null
            if (rainDrops > 0) {
                rain = this.project(0.1, angle, step.distance)
            }

            if (s == hit) {
                val textureX = Math.floor(texture.width * step.offset)
                val wall = this.project(step.height, angle, step.distance)

                batch.begin()
                batch.draw(texture, left.toFloat(), wall.top.toFloat(), width.toFloat(), wall.height.toFloat(), textureX.toInt(), 0, 1, texture.height, false, true)
                batch.end()

                com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND)
                com.badlogic.gdx.Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.setColor(0f, 0f, 0f, Math.max((step.distance + step.shading) / this.lightRange - map.light, 0.0).toFloat())
                shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled)
                shapeRenderer.rect(left.toFloat(), wall.top.toFloat(), width.toFloat(), wall.height.toFloat())
                shapeRenderer.end()
                com.badlogic.gdx.Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND)
            }

            if (rain != null) {
                com.badlogic.gdx.Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND)
                com.badlogic.gdx.Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.setColor(1f, 1f, 1f, 0.15f)
                shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled)
                while (--rainDrops > 0) {
                    shapeRenderer.rect(left.toFloat(), (Math.random() * rain.top).toFloat(), 1f, rain.height.toFloat())
                }
                shapeRenderer.end()
            }
        }
    }

    private fun project(height: Double, angle: Double, distance: Double): Projection {
        val z = distance * Math.cos(angle)
        val wallHeight = this.height * height / z
        val bottom = this.height / 2 * (1 + 1 / z)
        return Projection(bottom - wallHeight, wallHeight)
    }
}
