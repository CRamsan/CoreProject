package com.cramsan.framework.utils.format

actual class StringFormatter actual constructor() {
    actual fun format(string: String, vararg args: Any?): String {
        return string.format(*args)
    }
}
