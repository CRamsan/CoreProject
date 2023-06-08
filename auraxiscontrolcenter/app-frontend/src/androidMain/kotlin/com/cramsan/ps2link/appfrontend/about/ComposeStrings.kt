package com.cramsan.ps2link.appfrontend.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.appfrontend.R

@Composable
actual fun getAppName(): String = stringResource(R.string.app_name)

@Composable
actual fun getDevelopedBy(): String = stringResource(com.cramsan.ps2link.ui.R.string.text_developed_by)

@Composable
actual fun getAboutDescription(): String = stringResource(com.cramsan.ps2link.ui.R.string.text_about_description)

@Composable
actual fun getUrlHomePage(): String = stringResource(com.cramsan.ps2link.ui.R.string.url_homepage)

@Composable
actual fun getAboutThankYou(): String = stringResource(com.cramsan.ps2link.ui.R.string.text_about_thanks)
