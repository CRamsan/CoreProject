package com.cramsan.awsgame

class Ray(var map: Map,
          origin: Step,
          var sin: Double,
          var cos: Double,
          range: Double) {

    var steps: MutableList<Step> = mutableListOf()

    init {
        this.cast(origin, range)
    }

    fun cast(origin: Step, range: Double) {
        val stepX = step(sin, cos, origin.x, origin.y, false)
        val stepY = step(cos, sin, origin.y, origin.x, true)
        val nextStep = if (stepX.length2 < stepY.length2)
            inspect(stepX, 1.0, 0.0, origin.distance, stepX.y)
        else
            inspect(stepY, 0.0, 1.0, origin.distance, stepY.x)

        this.steps.add(origin)
        if (nextStep.distance < range) {
            this.cast(nextStep, range)
        }
    }

    fun step(rise: Double, run: Double, x: Double, y: Double, inverted: Boolean): Step {
        if (run == 0.0) return Step(0.0, 0.0, 0.0, 0.0, java.lang.Double.POSITIVE_INFINITY, 0.0, 0.0)
        val dx = if (run > 0) Math.floor(x + 1) - x else Math.ceil(x - 1) - x
        val dy = dx * (rise / run)
        return Step(if (inverted) y + dy else x + dx, if (inverted) x + dx else y + dy, 0.0, 0.0, dx * dx + dy * dy, 0.0, 0.0)
    }

    fun inspect(step: Step, shiftX: Double, shiftY: Double, distance: Double, offset: Double): Step {
        val dx: Double = if (cos < 0) shiftX else 0.0
        val dy: Double = if (sin < 0) shiftY else 0.0
        step.height = map.get((step.x - dx), step.y - dy)!!.toDouble()
        step.distance = distance + Math.sqrt(step.length2)
        if (shiftX == 1.0)
            step.shading = (if (cos < 0) 2 else 0).toDouble()
        else
            step.shading = (if (sin < 0) 2 else 1).toDouble()
        step.offset = offset - Math.floor(offset)
        return step
    }
}