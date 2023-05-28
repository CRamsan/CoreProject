package com.cramsan.framework.utils.format

/**
 * Android implementation of the [StringFormatter].
 * @see [StringFormatter] in common package.
 */
actual class StringFormatter actual constructor() {

    /**
     * @see [StringFormatter.format] in common package.
     */
    actual fun format(string: String, vararg args: Any?): String {
        return string.format(*args)
    }
}
