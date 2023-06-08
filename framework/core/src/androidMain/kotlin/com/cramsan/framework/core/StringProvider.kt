package com.cramsan.framework.core

import android.content.Context

/**
 * Actual implementation of the [StringProvider] for the Android platform. It takes a [context]
 * as a constructor argument to be able to expose the string resources. This context is expected
 * to be the application's context, as this will be valid for the entire life of the app.
 */
actual class StringProvider(
    val context: Context,
) {

    /**
     * Returns a string for the resource ID [resId].
     */
    fun getString(resId: Int) = context.getString(resId)
}
