package com.cramsan.awslib.utils.map

import com.cramsan.awslib.map.Cell

/**
 * Class to load maps from a CSV file.
 */
expect class MapLoader {
    /**
     * Load the map from the file [resourceName]. The returned value will be a matrix.
     */
    fun loadCSVMap(resourceName: String): Array<Array<Cell>>
}
