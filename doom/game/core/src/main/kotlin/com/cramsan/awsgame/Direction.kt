package com.cramsan.awsgame

enum class Direction(val direction: com.cramsan.awslib.enums.Direction) {

    NORTH(com.cramsan.awslib.enums.Direction.NORTH) {
        override fun angle(): Double {
            return Math.PI * -0.5
        }
    },
    SOUTH(com.cramsan.awslib.enums.Direction.SOUTH) {
        override fun angle(): Double {
            return Math.PI * 0.5
        }
    },
    EAST(com.cramsan.awslib.enums.Direction.EAST) {
        override fun angle(): Double {
            return Math.PI * 0.0
        }
    },
    WEST(com.cramsan.awslib.enums.Direction.WEST) {
        override fun angle(): Double {
            return Math.PI * 1.0
        }
    };

    abstract fun angle(): Double

    fun turnRight(): Direction {
        return when (direction) {
            com.cramsan.awslib.enums.Direction.NORTH -> EAST
            com.cramsan.awslib.enums.Direction.SOUTH -> WEST
            com.cramsan.awslib.enums.Direction.WEST -> NORTH
            com.cramsan.awslib.enums.Direction.EAST -> SOUTH
            com.cramsan.awslib.enums.Direction.KEEP -> TODO()
        }
    }

    fun turnLeft(): Direction {
        return when (direction) {
            com.cramsan.awslib.enums.Direction.NORTH -> WEST
            com.cramsan.awslib.enums.Direction.SOUTH -> EAST
            com.cramsan.awslib.enums.Direction.WEST -> SOUTH
            com.cramsan.awslib.enums.Direction.EAST -> NORTH
            com.cramsan.awslib.enums.Direction.KEEP -> TODO()
        }
    }

    fun turnAround(): Direction {
        return when (direction) {
            com.cramsan.awslib.enums.Direction.NORTH -> SOUTH
            com.cramsan.awslib.enums.Direction.SOUTH -> NORTH
            com.cramsan.awslib.enums.Direction.WEST -> EAST
            com.cramsan.awslib.enums.Direction.EAST -> WEST
            com.cramsan.awslib.enums.Direction.KEEP -> TODO()
        }
    }

    companion object {
        fun fromInternalDirection(direction: com.cramsan.awslib.enums.Direction): Direction {
            return when (direction) {
                com.cramsan.awslib.enums.Direction.NORTH -> NORTH
                com.cramsan.awslib.enums.Direction.SOUTH -> SOUTH
                com.cramsan.awslib.enums.Direction.WEST -> WEST
                com.cramsan.awslib.enums.Direction.EAST -> EAST
                com.cramsan.awslib.enums.Direction.KEEP -> TODO()
            }
        }
    }
}
