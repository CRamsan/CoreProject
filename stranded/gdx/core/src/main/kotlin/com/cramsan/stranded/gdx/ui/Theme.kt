package com.cramsan.stranded.gdx.ui

import com.badlogic.gdx.graphics.Color

object Theme {

    val containerPadding = Padding.large

    val containerSpacing = Padding.medium

    object Padding {
        val small = 3F

        val medium = 5F

        val large = 10F

        val xlarge = 20F
    }

    object Size {
        val xsmall = 5F

        val small = 15F

        val medium = 30F

        val large = 50F

        val xlarge = 75F
    }

    object Transtion {

        val xxfast = 0.2F

        val xfast = 0.5F

        val fast = 1F

        val normal = 2F

        val slow = 3F
    }

    object Scale {

        val unit = 90f

        val xsmall = unit / 4

        val small = unit / 2

        val medium = unit

        val large = unit * 2

        val xlarge = unit * 4

        val xxlarge = unit * 8
    }

    object Color {
        val background = Color(0x010101FF)

        val nightTop = Color(0x192B6AFF)

        val nightBottom = Color(0x0E2125FF)

        val dayTop = Color(0x32B5FFFF)

        val dayBottom = Color(0xFFF0D9FF.toInt())

        val sunsetTop = Color(0x010916FF)

        val sunsetBottom = Color(0x751500FF)
    }
}
