package com.cramsan.ps2link.appfrontend.profilepager.profile

import androidx.compose.runtime.Composable

@Composable
actual fun TextPrestige(prestige: Int): String {
    return "PRESTIGE: $prestige"
}

@Composable
actual fun TextStatus(): String {
    return "STATUS"
}

@Composable
actual fun TextLastLogin(): String {
    return "LAST LOGIN"
}

@Composable
actual fun TextOutfit(): String {
    return "OUTFIT"
}

@Composable
actual fun TextNone(): String {
    return "NONE"
}

@Composable
actual fun TextServer(): String {
    return "SERVER"
}

@Composable
actual fun TextHoursPlayed(): String {
    return "HOURS PLAYED"
}

@Composable
actual fun TextMembersSince(): String {
    return "MEMBERS SINCE"
}

@Composable
actual fun TextSessionsPlayer(): String {
    return "SESSIONS PLAYED"
}
