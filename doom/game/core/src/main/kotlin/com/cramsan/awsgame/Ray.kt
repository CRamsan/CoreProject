package com.cramsan.awsgame

import com.cramsan.awslib.enums.TerrainType
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class Ray(var map: Map,
          origin: Step,
          var sin: Double,
          var cos: Double,
          range: Double) {

    var steps: MutableList<Step> = mutableListOf()

    init {
        this.cast(origin, range)
    }

    private fun cast(origin: Step, range: Double) {
        val stepX = step(sin, cos, origin.x, origin.y, false)
        val stepY = step(cos, sin, origin.y, origin.x, true)
        val nextStep = if (stepX.length < stepY.length)
            inspect(stepX, 1.0, 0.0, origin.distance, stepX.y)
        else
            inspect(stepY, 0.0, 1.0, origin.distance, stepY.x)

        this.steps.add(origin)
        if (nextStep.distance < range) {
            this.cast(nextStep, range)
        }
    }

    private fun step(rise: Double, run: Double, x: Double, y: Double, inverted: Boolean): Step {
        if (run == 0.0) return Step(0.0, 0.0, 0.0, 0.0, java.lang.Double.POSITIVE_INFINITY, 0.0, 0.0)
        val dx = if (run > 0) floor(x + 1) - x else ceil(x - 1) - x
        val dy = dx * (rise / run)
        return Step(if (inverted) y + dy else x + dx, if (inverted) x + dx else y + dy, 0.0, 0.0, dx * dx + dy * dy, 0.0, 0.0)
    }

    private fun inspect(step: Step, shiftX: Double, shiftY: Double, distance: Double, offset: Double): Step {
        val dx: Double = if (cos < 0) shiftX else 0.0
        val dy: Double = if (sin < 0) shiftY else 0.0
        val cell = map.get((step.x - dx), step.y - dy)
        step.height = when (cell?.terrain) {
            TerrainType.OPEN -> 0.0
            TerrainType.WALL -> 1.0
            TerrainType.DOOR -> {
                if (cell.blocksMovement()) {
                    1.0
                } else {
                    0.0
                }
            }
            TerrainType.END -> 1.0
            null -> 0.0
        }
        step.distance = distance + sqrt(step.length)
        if (shiftX == 1.0)
            step.shading = (if (cos < 0) 2 else 0).toDouble()
        else
            step.shading = (if (sin < 0) 2 else 1).toDouble()
        step.offset = offset - floor(offset)
        return step
    }
}