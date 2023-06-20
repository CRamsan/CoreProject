package com.cramsan.ps2link.appfrontend.twitter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
actual fun TwitterHandleText(handle: String): String {
    return stringResource(
        id = com.cramsan.ps2link.ui.R.string.title_twitter_handle,
        formatArgs = arrayOf(handle),
    )
}
