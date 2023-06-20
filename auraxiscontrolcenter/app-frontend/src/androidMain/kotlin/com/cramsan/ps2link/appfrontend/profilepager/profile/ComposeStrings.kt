package com.cramsan.ps2link.appfrontend.profilepager.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
actual fun TextPrestige(prestige: Int): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.text_prestige, prestige)
}

@Composable
actual fun TextStatus(): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.text_status)
}

@Composable
actual fun TextLastLogin(): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.text_last_login)
}

@Composable
actual fun TextOutfit(): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.title_outfit)
}

@Composable
actual fun TextNone(): String {
    return stringResource(
        com.cramsan.ps2link.ui.R.string.text_none
    )
}

@Composable
actual fun TextServer(): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.title_server)
}

@Composable
actual fun TextHoursPlayed(): String {
    return stringResource(com.cramsan.ps2link.ui.R.string.text_hours_played)
}

@Composable
actual fun TextMembersSince(): String {
    return stringResource(
        com.cramsan.ps2link.ui.R.string.text_member_since
    )
}

@Composable
actual fun TextSessionsPlayer(): String {
    return stringResource(
        com.cramsan.ps2link.ui.R.string.text_sessions_played
    )
}
