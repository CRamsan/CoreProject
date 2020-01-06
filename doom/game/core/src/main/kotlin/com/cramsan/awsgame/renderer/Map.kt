package com.cramsan.awsgame.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.map.MapLoader
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class Map(private var size: Int) {
    var light: Double = 0.toDouble()
    var skybox: Texture
    var wallTexture: Texture
    var map: GameMap

    init {
        this.light = 0.0
        this.skybox = Texture(Gdx.files.internal("deathvalley_panorama.jpg"))
        this.wallTexture = Texture(Gdx.files.internal("wall_texture.jpg"))
        map = GameMap(MapLoader().loadCSVMap(Gdx.files.internal("map1.txt").file().absolutePath))
    }

    operator fun get(px: Double, py: Double): Cell? {
        var x = px
        var y = py
        x = floor(x)
        y = floor(y)
        return if (x < 0 || x > this.size - 1 || y < 0 || y > this.size - 1)
            null
        else
            this.map.cellAt(x.toInt(), y.toInt())
    }

    fun cast(point: Point, angle: Double, range: Double): Ray {
        return Ray(
            this, Step(point.x, point.y, 0.0, 0.0, 0.0, 0.0, 0.0),
            sin(angle),
            cos(angle),
            range
        )
    }

    fun update() {
        this.light = 0.0
    }
}
