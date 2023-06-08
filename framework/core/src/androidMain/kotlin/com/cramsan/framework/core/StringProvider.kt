package com.cramsan.framework.core

import android.content.Context

actual class StringProvider(
    val context: Context,
) {

    fun getString(resId: Int) = context.getString(resId)

}