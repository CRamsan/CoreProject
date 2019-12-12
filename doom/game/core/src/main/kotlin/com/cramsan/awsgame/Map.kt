package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.map.MapLoader

class Map(protected var size: Int) {
    var light: Double = 0.toDouble()
    var skybox: com.badlogic.gdx.graphics.Texture
    var wallTexture: com.badlogic.gdx.graphics.Texture
    var map: GameMap

    init {
        this.light = 0.0
        this.skybox = com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal("deathvalley_panorama.jpg"))
        this.wallTexture = com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal("wall_texture.jpg"))
        map = GameMap(MapLoader().loadCSVMap(Gdx.files.internal("map1.txt").file().absolutePath))
    }

    operator fun get(px: Double, py: Double): Int? {
        var x = px
        var y = py
        x = Math.floor(x)
        y = Math.floor(y)
        return if (x < 0 || x > this.size - 1 || y < 0 || y > this.size - 1) -1 else this.map.cellAt(x.toInt(),
            y.toInt()
        ).terrain.value
    }

    fun cast(point: Point, angle: Double, range: Double): Ray {
        return Ray(this, Step(point.x, point.y, 0.0, 0.0, 0.0, 0.0, 0.0), Math.sin(angle), Math.cos(angle), range)
    }

    fun update(seconds: Double) {
        if (this.light > 0)
            this.light = Math.max(this.light - 10 * seconds, 0.0)
        else if (Math.random() * 5 < seconds) this.light = 2.0
    }
}
