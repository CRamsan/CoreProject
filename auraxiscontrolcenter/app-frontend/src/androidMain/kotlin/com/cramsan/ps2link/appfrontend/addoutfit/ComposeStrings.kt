package com.cramsan.ps2link.appfrontend.addoutfit

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
actual fun TagText(): String = stringResource(com.cramsan.ps2link.ui.R.string.text_tag)

@Composable
actual fun OutfitNameText(): String = stringResource(com.cramsan.ps2link.ui.R.string.text_outfit_name)

@Composable
actual fun MemberCountText(memberCount: Int): String = stringResource(
    com.cramsan.ps2link.ui.R.string.text_outfit_members,
    memberCount,
)
