package com.cramsan.awsgame

class Player(protected var x: Double, protected var y: Double, var direction: Double) {
    var paces: Double = 0.toDouble()

    var weapon: com.badlogic.gdx.graphics.Texture

    init {
        this.paces = 0.0
        this.weapon = com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal("knife_hand.png"))
    }

    fun rotate(angle: Double) {
        this.direction = (this.direction + angle + AWSGame.CIRCLE) % AWSGame.CIRCLE
    }

    fun walk(distance: Double, map: Map) {
        val dx = Math.cos(this.direction) * distance
        val dy = Math.sin(this.direction) * distance
        if (map.get(this.x + dx, this.y)!! <= 0) this.x += dx
        if (map.get(this.x, this.y + dy)!! <= 0) this.y += dy
        this.paces += distance
    }

    fun update(controls: Controls, map: Map, seconds: Double) {
        if (controls.left) this.rotate(-Math.PI * seconds)
        if (controls.right) this.rotate(Math.PI * seconds)
        if (controls.forward) this.walk(3 * seconds, map)
        if (controls.backward) this.walk(-3 * seconds, map)
    }

    fun toPoint(): Point {
        return Point(this.x, this.y)
    }
}
