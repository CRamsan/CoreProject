package com.cramsan.stranded.gdx.ui

import ktx.graphics.color

object Theme {

    val backgroundColor = color(0.15f, 0.15f, 0.1f)

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
}
