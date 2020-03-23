package com.cramsan.awslib.utils.map

import com.cramsan.awslib.map.Cell

expect class MapLoader {
    fun loadCSVMap(resourceName: String): Array<Array<Cell>>
}
