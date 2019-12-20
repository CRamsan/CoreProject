package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.math.*

class Camera(private val camera: OrthographicCamera, private var resolution: Double, private var fov: Double) {
    private var width: Double = 0.toDouble()
    private var height: Double = 0.toDouble()
    private var spacing: Double = 0.toDouble()
    private var range: Double = 0.toDouble()
    private var lightRange: Double = 0.toDouble()
    private var scale: Double = 0.toDouble()
    private val batch: SpriteBatch = SpriteBatch()
    private val shapeRenderer: ShapeRenderer

    init {
        this.batch.projectionMatrix = camera.combined
        this.shapeRenderer = ShapeRenderer()
        this.shapeRenderer.projectionMatrix = camera.combined
        this.width = this.camera.viewportWidth.toDouble()
        this.height = this.camera.viewportHeight.toDouble()
        this.spacing = this.width / resolution
        this.range = 14.0
        this.lightRange = 5.0
        this.scale = (this.width + this.height) / 1200
    }

    fun render(player: Player, map: Map) {
        this.drawColumns(player, map)
        this.drawWeapon(player.weapon, player.paces)
    }

    private fun drawColumns(player: Player, map: Map) {
        var column = 0
        while (column < this.resolution) {
            val angle = this.fov * (column / this.resolution - 0.5)
            val ray = map.cast(player.toPoint(), player.angle() + angle, this.range)
            this.drawColumn(column.toDouble(), ray, angle, map)
            column++
        }
    }

    private fun drawWeapon(weapon: Texture, paces: Double) {
        val bobX = cos(paces * 2) * this.scale * 6.0
        val bobY = sin(paces * 4) * this.scale * 6.0
        val left = this.width * 0.66 + bobX
        val top = this.height * 0.6 + bobY
        batch.begin()
        batch.draw(weapon, left.toFloat(), top.toFloat(), (weapon.width * this.scale).toFloat(), (weapon.height * this.scale).toFloat(), 0, 0, weapon.width, weapon.height, false, true)
        batch.end()
    }

    private fun drawColumn(column: Double, ray: Ray, angle: Double, map: Map) {
        val texture = map.wallTexture
        val left = floor(column * this.spacing)
        val width = ceil(this.spacing)
        var hit = -1

        while (++hit < ray.steps.size && ray.steps[hit].height <= 0) {
        }

        for (s in ray.steps.indices.reversed()) {
            val step = ray.steps[s]

            if (s == hit) {
                val textureX = floor(texture.width * step.offset)
                val wall = this.project(step.height, angle, step.distance)

                batch.begin()
                batch.draw(texture, left.toFloat(), wall.top.toFloat(), width.toFloat(), wall.height.toFloat(), textureX.toInt(), 0, 1, texture.height, false, true)
                batch.end()

                Gdx.gl.glEnable(GL20.GL_BLEND)
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.setColor(0f, 0f, 0f, max((step.distance) / this.lightRange - map.light, 0.0).toFloat())
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                shapeRenderer.rect(left.toFloat(), wall.top.toFloat(), width.toFloat(), wall.height.toFloat())
                shapeRenderer.end()
                Gdx.gl.glDisable(GL20.GL_BLEND)
            }
        }
    }

    private fun project(height: Double, angle: Double, distance: Double): Projection {
        val z = distance * cos(angle)
        val wallHeight = this.height * height / z
        val bottom = this.height / 2 * (1 + 1 / z)
        return Projection(bottom - wallHeight, wallHeight)
    }
}
