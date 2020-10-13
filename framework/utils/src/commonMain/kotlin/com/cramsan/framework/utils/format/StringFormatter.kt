package com.cramsan.framework.utils.format

expect class StringFormatter() {

    fun format(string: String, vararg args: Any?): String
}
