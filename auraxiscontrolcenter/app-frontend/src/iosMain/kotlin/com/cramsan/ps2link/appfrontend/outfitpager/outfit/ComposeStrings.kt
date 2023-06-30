package com.cramsan.ps2link.appfrontend.outfitpager.outfit

import androidx.compose.runtime.Composable

@Composable
actual fun OutfitCreatedText(): String {
    return "CREATED"
}

@Composable
actual fun OutfitTagText(tag: String): String {
    return "[$tag]"
}

@Composable
actual fun OutfitLeaderText(): String {
    return "LEADER"
}

@Composable
actual fun OutfitMembersText(): String {
    return "MEMBERS"
}
