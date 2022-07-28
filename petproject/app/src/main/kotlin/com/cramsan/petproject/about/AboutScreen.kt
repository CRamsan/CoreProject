package com.cramsan.petproject.about

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.petproject.R

/**
 * Render the About screen for the [AboutFragment].
 */
@Composable
fun AboutScreen() {
    Column {
        Text(stringResource(R.string.about_licence_flaticon))
    }
}
