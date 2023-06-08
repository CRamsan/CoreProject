package com.cramsan.ps2link.appfrontend

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.ui.R

@Composable
actual fun UnknownErrorText(): String = stringResource(R.string.text_unkown_error)

@Composable
actual fun UnknownText(): String = stringResource(R.string.text_unknown)