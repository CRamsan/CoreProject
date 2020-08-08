package com.cramsan.awslib.enums

import com.cramsan.awslib.utils.constants.TerrainIds

/**
 *
@Deprecated("This Enum will be removed in the future",
 ReplaceWith("Instead of relying on this enum, we should use the tile's own properties"),
 DeprecationLevel.WARNING)
 */
enum class TerrainType(val value: Int) {
    OPEN(TerrainIds.OPEN),
    WALL(TerrainIds.WALL),
    DOOR(TerrainIds.DOOR),
    END(TerrainIds.END)
}
