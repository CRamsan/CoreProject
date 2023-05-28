package com.cramsan.framework.sample.android.app.homepage

/**
 *
 */
data class HomePageUIState(
    val loading: Boolean = false,
    val title: String? = null,
    val subtitle: String? = null,
    val message: String? = null,
) {
    companion object {
        val InitialState = HomePageUIState(
            false,
            null,
            null,
            null,
        )
    }
}
